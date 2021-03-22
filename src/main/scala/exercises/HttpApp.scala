package exercises

import org.http4s._
import org.http4s.dsl.request._
import zio.Task
import zio.interop.catz.implicits._

object HttpApp {

  val helloRoute: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "hello" / name => Task(Response(Status.Ok).withBody(s"Hello, $name from ZIO on a server!"))
  }

}