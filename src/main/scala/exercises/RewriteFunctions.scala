package exercises

import zio.console._
import zio.{ExitCode, URIO, ZIO}

object RewriteFunctions extends zio.App {
  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    for {
      _ <- getStrLnForComprehension().exitCode
      exit <- randomGuessForComprehension().exitCode
    } yield exit

  def putStrLn(line: String) = ZIO.effect(println(line))

  def getStrLnFlatMap():  ZIO[Console, Throwable, Unit]= {
    putStrLn("What is your name?").flatMap(_ =>
      getStrLn.flatMap(name =>
        putStrLn(s"Hello, ${name}!")))
  }

  // Rewrite the following ZIO code above that uses flatMap into a for comprehension

  def getStrLnForComprehension(): ZIO[Console, Throwable, Unit] = {
    for {
      _ <- putStrLn("What is your name?")
      name <- getStrLn
      _ <- putStrLn(s"Hello, $name!")
    } yield ()
  }

  val random = ZIO.effect(scala.util.Random.nextInt(3) + 1)

  def randomGuessFlatMap(): ZIO[Console, Throwable, Unit] = {
    random.flatMap(int =>
      putStrLn("Guess a number from 1 to 3:").flatMap(_ =>
        getStrLn.flatMap(num =>
          if (num == int.toString) putStrLn("You guessed right!")
          else putStrLn(s"You guessed wrong, the number was $int!")))
    )
  }

  // Rewrite the following ZIO code above that uses flatMap into a for comprehension

  def randomGuessForComprehension(): ZIO[Console, Throwable, Unit] = {
    for {
      int <- random
      _ <- putStrLn("Guess a number from 1 to 3:")
      num <- getStrLn
      _ <- {
        if (num == int.toString) putStrLn(s"You guessed $int right!")
        else putStrLn(s"You guessed wrong, the number was $int!")
      }
    } yield ()
  }

}
