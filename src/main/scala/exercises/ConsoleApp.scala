package exercises

import zio._
import zio.console._
import java.io.IOException

import scala.util.Random

object ConsoleApp extends zio.App {
  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    guess.exitCode

  def guess(): ZIO[Console, Throwable, Unit] = {
    for {
    random <- ZIO.effect(Random.nextInt(3) + 1)
    _ <- putStrLn("Guess a number between 1 and 3")
    guess <- getStrLn
    _ <- if (guess == random.toString) putStr(s"You guessed $guess right!")
    else putStrLn(s"You guessed $guess, correct answer was $random")
    } yield ()
  }

  def readUntil(
    acceptInput: String => Boolean
               ): ZIO[Console, IOException, String] =
    for {
    input <- getStrLn
    res <-
      if (acceptInput(input)) ZIO.succeed(input)
      else readUntil(acceptInput)
    } yield res

  def doWhile[R, E, A](
                        body: ZIO[R, E, A]
                      )(condition: A => Boolean): ZIO[R, E, A] =
    for {
      flag <- body
      z <-
        if (condition(flag)) ZIO.succeed(flag)
        else doWhile(body)(condition)
    } yield z
}
