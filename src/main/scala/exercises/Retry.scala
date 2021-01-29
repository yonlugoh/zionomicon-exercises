package exercises

import zio.console.Console
import zio.{ExitCode, RIO, URIO, ZIO, console}

object Retry extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    readIntOrRetry.exitCode
  }

  val readInt: RIO[Console, Int] =
      for {
        line <- console.getStrLn
        int <- ZIO.effect(line.toInt)
      } yield int

  // Recursive
  lazy val readIntOrRetry: URIO[Console, Int] =
    readInt
      .orElse(console.putStrLn("\nPlease enter a valid integer"))
      .zipRight(readIntOrRetry)

}
