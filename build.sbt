import sbt.Project.projectToRef

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val customScalacOptions = Seq(
//  "-Ymacro-debug-lite",
//  "-deprecation", // Emit warning and location for usages of deprecated APIs.
//  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
//  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
//  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
//  "-Xlint", // Enable recommended additional warnings.
//  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
//  "-Ywarn-dead-code", // Warn when dead code is identified.
//  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
//  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
//  "-Ywarn-numeric-widen" // Warn when numerics are widened.

)

lazy val databaseUrl = sys.env.getOrElse("DB_DEFAULT_URL", "DB_DEFAULT_URL is not set")
lazy val databaseUser = sys.env.getOrElse("DB_DEFAULT_USER", "DB_DEFAULT_USER is not set")
lazy val databasePassword = sys.env.getOrElse("DB_DEFAULT_PASSWORD", "DB_DEFAULT_PASSWORD is not set")
lazy val updateDb = taskKey[Seq[File]]("Runs flyway and Slick code codegeneration.")

lazy val postgresSlick = (project in file("postgres-slick")).settings(
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "3.0.3"
    , "com.github.tminglei" %% "slick-pg" % "0.10.0-M1"
    , "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
    , "com.typesafe.slick" %% "slick-codegen" % "3.0.0"
  )
)

lazy val flyway = (project in file("flyway"))
  .settings(flywaySettings:_*)
  .settings(
    scalaVersion := scalaV,
    flywayUrl := databaseUrl,
    flywayUser := databaseUser,
    flywayPassword := databasePassword,
    flywayLocations := Seq("filesystem:server/conf/db/migration/default"),
    flywaySchemas := Seq("public"),
    updateDb <<= (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
      toError(r.run("com.github.olafurpg.slick.Codegen", cp.files, Array[String](), s.log))
      Seq()
    }
  ).dependsOn(postgresSlick)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
    , "com.github.japgolly.scalajs-react" %%% "core" % "0.9.2"
    , "com.lihaoyi" %%% "autowire" % "0.2.5"
  ),
  jsDependencies ++= Seq(
    "org.webjars" % "react" % "0.12.2" / "react-with-addons.js" commonJSName "React"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val server = (project in file("server"))
  .settings(
    scalaJSProjects := clients,
    pipelineStages := Seq(scalaJSProd)
  ).
  enablePlugins(PlayScala)
  .settings(
    scalaVersion := scalaV,
    scalacOptions ++= customScalacOptions,
    libraryDependencies ++= Seq(
      jdbc
      , "com.github.tminglei" %% "slick-pg" % "0.10.0-M1"
      , "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
      , "com.typesafe.play" %% "play-slick" % "1.1.0-RC2"
      , "com.typesafe.slick" %% "slick" % "3.1.0-RC2"
      , "com.vmunier" %% "play-scalajs-scripts" % "0.3.0"
      , "org.scalatest" %% "scalatest" % "2.2.1" % "test"
      , "org.scalatestplus" %% "play" % "1.4.0-M3" % "test"
      , "com.lihaoyi" %% "autowire" % "0.2.5"
      , "com.mohiva" %% "play-silhouette" % "3.0.4"
      , "com.mohiva" %% "play-silhouette-testkit" % "3.0.4" % "test"
    )
).enablePlugins(PlayScala)
.aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm, postgresSlick)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "0.3.6"
    )
).
jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

fork in run := true