package exercises

import zio._

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

}