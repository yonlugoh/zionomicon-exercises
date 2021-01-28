package exercises

import zio.ZIO

object RewriteFunctions {
  def printLine(line: String) = ZIO.effect(println(line))
  val readLine = ZIO.effect(scala.io.StdIn.readLine())

  def readLineFlatMap(): Unit = {
    printLine("What is your name?").flatMap(_ =>
      readLine.flatMap(name =>
        printLine(s"Hello, ${name}!")))
  }

  // Rewrite the following ZIO code above that uses flatMap into a for comprehension

  def readLineForComprehension(): Unit = {
    for {
      name <- ZIO.effect(scala.io.StdIn.readLine())
      _ <- printLine(s"Hello, $name!")
    } yield ()
  }

  val random = ZIO.effect(scala.util.Random.nextInt(3) + 1)

  def randomGuessFlatMap(): Unit = {
    random.flatMap(int =>
      printLine("Guess a number from 1 to 3:").flatMap(_ =>
        readLine.flatMap(num =>
          if (num == int.toString) printLine("You guessed right!")
          else printLine(s"You guessed wrong, the number was $int!")))
    )
  }

  def randomGuessForComprehension(): Unit = {
    for {
      int <- random
      _ <- printLine("Guess a number from 1 to 3:")
      num <- readLine
      _ <- {
      if (num == int.toString) printLine("You guessed right!")
      else printLine(s"You guessed wrong, the number was $int!")
    }
    } yield ()
  }

}
