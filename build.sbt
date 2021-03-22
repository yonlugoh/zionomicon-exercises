name := "zio-experiments"

version := "1.0"

val zioV = "1.0.5"

libraryDependencies ++=
  Seq(
      "dev.zio" %% "zio"        % zioV,
    "dev.zio" %% "zio-test" % zioV,
    "dev.zio" %% "zio-test-sbt" % zioV,
    "dev.zio" %% "zio-interop-cats" % zioV,
    "org.http4s" %% "http4s-server" % "1.0.0-M19",
    "org.http4s" %% "http4s-blaze-server" % "1.0.0-M19",
  )

