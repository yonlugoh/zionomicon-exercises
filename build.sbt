name := "zio-experiments"

version := "1.0"

val zioV = "1.0.4"

libraryDependencies ++=
  Seq(
      "dev.zio" %% "zio"        % zioV,
      "dev.zio" %% "zio-test"   % zioV
  )

