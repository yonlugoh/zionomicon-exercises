package retry

import zio.console.Console
import zio.{RIO, URIO, ZIO, console}

object Retry {
    val readInt: RIO[Console, Int] =
      for {
        line <- console.getStrLn
        int <- ZIO.effect(line.toInt)
      } yield int

    lazy val readIntOrRetry: URIO[Console, Int] =
      readInt
        .orElse(console.putStrLn("\nPlease enter a valid integer"))
        .zipRight(readIntOrRetry)

    def run(args: List[String]) =
      readIntOrRetry.exitCode
}
