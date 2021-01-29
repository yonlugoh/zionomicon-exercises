package exercises

import zio._

/**
  * Implementing utils using ZIO library itself
  */
object ZIOUtils {
  def eitherToZIO[E, A](either: Either[E, A]): ZIO[Any, E, A] =
    either match {
      case Left(x) => ZIO.fail(x)
      case Right(x) => ZIO.succeed(x)
    }

  def listToZIO[A](list: List[A]): ZIO[Any, None.type, A] =
    list match {
      case Nil    => ZIO.fail(None)
      case x :: _ => ZIO.succeed(x)
    }

  def currentTime(): Long = System.currentTimeMillis()

  lazy val currentTimeZIO: ZIO[Any, Nothing, Long] =
    ZIO.effectTotal(currentTime())

  def getCacheValue(
                     key: String,
                     onSuccess: String => Unit,
                     onFailure: Throwable => Unit
                   ): Unit = ???

  def getCacheValueZio(key: String): ZIO[Any, Throwable, String] =
    ZIO.effectAsync[Any, Throwable, String] {
      cb =>
        getCacheValue(key, r => cb(ZIO.succeed(r)), ex => cb(ZIO.fail(ex)))
    }

  trait User
  def saveUserRecord(
                      user: User,
                      onSuccess: () => Unit,
                      onFailure: Throwable => Unit
                    ): Unit =
    ???

  def saveUserRecordZio(user: User): ZIO[Any, Throwable, Unit] =
    ZIO.effectAsync[Any, Throwable, Unit] {
      cb =>
        saveUserRecord(user, () => cb(ZIO.succeed()), ex => cb(ZIO.fail(ex)) )
    }

}
