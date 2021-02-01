package app

import java.io.IOException

import zio.{ExitCode, URIO, ZIO}
import zio.console._

object HelloWorld extends zio.App {
  def run(args: List[String]): URIO[Console, ExitCode] =
    myAppLogic.exitCode

  val greet: ZIO[Console, Nothing, Unit] =
    for {
      name <- getStrLn.orDie
      _ <- putStrLn(s"Hello, $name!")
    } yield ()

  val myAppLogic: ZIO[Console, IOException, Unit] =
    for {
      _    <- putStrLn("Hello! What is your name?")
      name <- getStrLn
      _    <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    } yield ()
}