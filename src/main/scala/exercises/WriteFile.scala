package write

import zio.{Task, ZIO}
import zio.console._

object WriteFile extends zio.App {
  def run(args: List[String]): zio.URIO[zio.ZEnv, zio.ExitCode] =
    copyAndWrite.exitCode

  // 1
  def readFile(file: String): String = {
    val source = scala.io.Source.fromFile(file)
    try source.getLines.mkString
    finally source.close()
  }


  def readFileZio(file: String): Task[String] = ZIO.effect(readFile(file))

  // 2
  def writeFile(file: String, text: String): Unit = {
    import java.io._
    val fileObj: File = new File(file)
    fileObj.createNewFile()
    val pw = new PrintWriter(fileObj)
    try pw.write(text) finally pw.close
  }

  def writeFileZio(file: String, text: String): Task[Unit] = ZIO.effect(writeFile(file, text))

  // 3
  def copyFileZio(source: String, dest: String): Task[Unit] =
    for {
      content <- readFileZio(source)
      _       <- writeFileZio(dest, content)
    }
  yield ()

  def copyAndWrite =
    for {
      _ <- copyFileZio("build.sbt", "/tmp/build.sbt.cpy")
      copied <- readFileZio("/tmp/build.sbt.cpy")
      _ <- putStrLn(copied)
    }
  yield ()
}
