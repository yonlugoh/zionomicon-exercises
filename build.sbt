name := "zio-experiments"

version := "1.0"

val zioV = "1.0.4"

val http4sV = "1.0.0-M19"

libraryDependencies ++=
  Seq(
      "dev.zio" %% "zio"        % zioV,
    "dev.zio" %% "zio-test" % zioV,
    "dev.zio" %% "zio-test-sbt" % zioV,
    "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC12",
    "org.http4s" %% "http4s-server" % http4sV,
    "org.http4s" %% "http4s-blaze-server" % http4sV,
    "org.http4s" %% "http4s-dsl"          % http4sV,
    "org.http4s" %% "http4s-circe" % http4sV,
  )

