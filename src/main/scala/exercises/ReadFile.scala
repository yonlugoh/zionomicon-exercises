package exercises

import zio.{ExitCode, URIO, ZIO}
import java.io._
import zio.console.Console
import scala.io.Source._
object ReadFile {

  def readFile(file: String): String = {
    val source = fromFile(file)
    try source.getLines.mkString finally source.close()
  }

  def readFileZio(file: String): URIO[Any with Console, ExitCode] =
    ZIO.effect(readFile(file)).exitCode
}
