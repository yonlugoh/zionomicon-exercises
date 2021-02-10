package exercises

import zio._
import zio.console._

object ErrorHandling extends zio.App {
  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    divisionByZero.exitCode
  }

  def divisionByZero: UIO[Int] =
    UIO.effectTotal(1 / 0)

  final case class ApiError(message: String) extends Exception(message)
  final case class DbError(message: String) extends Exception(message)

  trait Result
  lazy val callApi: ZIO[Any, ApiError, String] = ???
  lazy val queryDb: ZIO[Any, DbError, Int] = ???

  // Error type becomes the lowest common ancestor, which is Exception
  lazy val combine: ZIO[Any, Exception, (String, Int)] = callApi.zip(queryDb)

  final case class InsufficientPermission(user: String, operation: String)
  final case class FileIsLocked(file: String)

  def shareDocument(doc: String): ZIO[Any, InsufficientPermission, Unit] = ???
  def moveDocument(doc: String, folder: String): ZIO[Any, FileIsLocked, Unit] = ???

  // Error type becomes Any, which means that we have no type information of the error channel.
  // We cannot safely do anything with a value of type Any, so the most we can say about such an effect
  // is that it can fail for some unknowable reason
  lazy val result: ZIO[Any, Any, (Unit, Unit)] = shareDocument("347823").zip(moveDocument("347823", "/temp/"))

  trait DatabaseError
  trait UserProfile

  def lookupProfile(userId: String): ZIO[Any, DatabaseError, Option[UserProfile]] = ???

  // Shift Option[UserProfile] to Option[DatabaseError]. If original effect failed with None, then the new effect will fail with None
  // If original effect failed with some error e, then new effect will fail with Some(e)
  def lookupProfile2(userId: String): ZIO[Any, Option[DatabaseError], UserProfile] =
    lookupProfile(userId).foldM(
      error => ZIO.fail(Some(error)),
      success => success match {
        case None => ZIO.fail(None)
        case Some(profile) => ZIO.succeed(profile)
      }
    )

  // implementing lookupProfile2 in more succinct manner.
  def lookupProfile3(userId: String): ZIO[Any, Option[DatabaseError], UserProfile] =
    lookupProfile(userId).some

  def failWithMessage(string: String): ZIO[Any, Nothing, Error] =
    ZIO.succeed(throw new Error(string))

  // Fixed version
  def failWithMessageFixed(string: String): ZIO[Any, Error, Nothing] =
    ZIO.fail(throw new Error(string))

  def recoverFromSomeDefects[R, E, A](zio: ZIO[R, E, A])(f: Throwable => Option[A]): ZIO[R, E, A] =
    zio.foldCauseM(
      cause =>
        cause
          .defects
          .headOption
          .flatMap(f(_))
          .map(ZIO.succeed(_))
          .getOrElse(zio),
      _ => zio
    )

  def logFailures[R, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] =
    zio.foldCauseM(
      cause => ZIO.effectTotal(putStrLn(cause.prettyPrint)) *> zio,
      _ => zio
    )

  val effect = ZIO.effectTotal(1/0)
  val recovered = recoverFromSomeDefects(effect) {
    t => Some(t.getMessage.length)
  }

  def onAnyFailure[R, E, A](zio: ZIO[R, E, A], handler: ZIO[R, E, Any]): ZIO[R, E, A] =
    zio.foldCauseM(
      _ => handler *> zio,
      success => ZIO.succeed(success)
    )

  def ioException[R, A](zio: ZIO[R, Throwable, A]): ZIO[R, java.io.IOException, A] =
    zio.refineOrDieWith {
      case x: java.io.IOException => x
    } (identity)

  val parseNumber: ZIO[Any, Throwable, Int] = ZIO.effect("foo".toInt)

  val parseNumberRefined: ZIO[Any, NumberFormatException, Int] =
    parseNumber.refineToOrDie[NumberFormatException]

  def left[R, E, A, B](zio: ZIO[R, E, Either[A, B]]): ZIO[R, Either[E, B], A] =
    zio.foldM(
      failure =>  ZIO.fail(Left(failure)),
      {
        case Right(b) => ZIO.fail(Right(b))
        case Left(a) => ZIO.succeed(a)
      }
    )

  def unleft[R, E, A, B](zio: ZIO[R, Either[E, B], A]): ZIO[R, E, Either[A, B]] =
    zio.foldM(
      {
        case Right(b) => ZIO.succeed(Right(b))
        case Left(e) => ZIO.fail(e)
      },
      success => ZIO.succeed(Left(success))
    )

  def right[R, E, A, B](zio: ZIO[R, E, Either[A, B]]): ZIO[R, Either[E, A], B] =
    zio.foldM(
      failure => ZIO.fail(Left(failure)),
      {
        case Left(a) => ZIO.fail(Right(a))
        case Right(b) => ZIO.succeed(b)
      }
    )

  def unright[R, E, A, B](zio: ZIO[R, Either[E, A], B]): ZIO[R, E, Either[A, B]] =
    zio.foldM(
      {
        case Left(e) => ZIO.fail(e)
        case Right(a) => ZIO.succeed(Left(a))
      },
      success => ZIO.succeed(Right(success))
    )
}