package exercises

import utils.Utils.foreach
import utils.WriteFile._
import zio.{ App => ZIOApp, ZIO}
import zio.console._

/**
  * Run ```sbt runMain Cat **list of files**```
  */
object Cat extends ZIOApp {
  def program(fileNames: List[String]) =
    ZIO.foreach(fileNames)(readFileZio(_).flatMap(putStrLn(_)))

  def run(args: List[String]) = program(args).exitCode
}
