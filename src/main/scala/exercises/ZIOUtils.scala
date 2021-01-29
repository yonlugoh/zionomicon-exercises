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
}
