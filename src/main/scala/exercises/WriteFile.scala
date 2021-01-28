package exercises

import zio.{Task, ZIO}

object WriteFile {

  def writeFile(file: String, text: String): Unit = {
    import java.io._
    val pw = new PrintWriter(new File(file))
    try pw.write(text) finally pw.close
  }

  def writeFileZio(file: String, text: String): Task[Unit] =
    ZIO.effect(writeFile(file, text))
}
