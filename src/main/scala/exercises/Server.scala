package exercises

import exercises.HttpApp.httpApp
import org.http4s.server.blaze.BlazeServerBuilder
import zio._

// TODO: can't compile
object Server extends zio.App {
  val server: ZManaged[Any, Nothing, Nothing] = ZIO.runtime[Any].toManaged_.flatMap { implicit runtime =>
    BlazeServerBuilder[Task](runtime.platform.executor.asEC)
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .resource
      .toManagedZIO
  }

  val useServer: Task[Nothing] = server.useForever

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    useServer.exitCode

}
