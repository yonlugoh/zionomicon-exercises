package exercises

import ReadFile._
import WriteFile._
import zio.{Task, ZIO}

object CopyFile {
  def copyFile(source: String, dest: String): Unit = {
    val contents = readFile(source)
    writeFile(dest, contents)
  }

  def copyFileZio(source: String, dest: String): Task[Unit] = {
    readFileZio(source).flatMap(contents => ZIO.effect(writeFileZio(dest, contents)))
  }

  def copyFileZioForComprehension(source: String, dest: String): Task[Unit] = {
    for {
    contents <- readFileZio(source)
    _ <- writeFileZio(dest, contents)
    } yield ()
  }
}
