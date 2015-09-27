import sbt.Project.projectToRef
import slick.codegen.SourceCodeGenerator
import slick.{ model => m }

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val databaseUrl = sys.env.getOrElse("DB_DEFAULT_URL", "DB_DEFAULT_URL is not set")
lazy val databaseUser = sys.env.getOrElse("DB_DEFAULT_USER", "DB_DEFAULT_USER is not set")
lazy val databasePassword = sys.env.getOrElse("DB_DEFAULT_PASSWORD", "DB_DEFAULT_PASSWORD is not set")
lazy val codegenDriver = scala.slick.driver.H2Driver
lazy val jdbcDriver = "org.h2.Driver"
lazy val sharedSourceGenerator = (model:  m.Model) =>
      new SourceCodeGenerator(model) {

        override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]) : String = {
          s"""
             |package ${pkg}
             |
             |import com.geirsson.util.Epoch
             |
             |$code
          """.stripMargin
        }
        override def code =
          tables.map(_.code.mkString("\n")).mkString("\n\n")
        override def tableName = (dbName: String) => dbName + "Table"
        override def entityName = (dbName: String) => dbName
        // disable entity class generation and mapping
        override def Table = new Table(_) {
            override def PlainSqlMapper = new PlainSqlMapper {
              override def doc = ""
              override def code = ""
            }
          override def TableClass = new TableClass {
            override def doc = ""
            override def code = ""
          }
          override def TableValue = new TableValue {
            override def doc = ""
            override def code = ""
          }
            override def Column = new Column(_) {
              override def rawType = model.tpe match {
                case "java.sql.Timestamp" => "Epoch" // kill j.s.Timestamp
                case _ =>
                  super.rawType
              }
              override def rawName: String = model.name
            }
          }
      }
lazy val serverSourceGenerator = (model:  m.Model) =>
      new SourceCodeGenerator(model) {
        override def tableName = (dbName: String) => dbName + "Table"
        override def entityName = (dbName: String) => dbName
        override def code =
        s"""
          |import com.geirsson.util.Epoch
          |implicit val dateTimeMapper =  MappedColumnType.base[Epoch, java.sql.Timestamp](
          | { epoch =>  new java.sql.Timestamp(epoch.millis) },
          | { ts    =>  Epoch(ts.getTime()) }
          |)
          |""".stripMargin + super.code
        // disable entity class generation and mapping
        override def Table = new Table(_) {
          override def EntityType = new EntityType{
            override def doc = ""
            override def code = ""
          }
          override def Column = new Column(_) {
            override def rawType = model.tpe match {
              case "java.sql.Timestamp" => "Epoch" // kill j.s.Timestamp
              case _ =>
                super.rawType
            }
            override def rawName: String = model.name
          }
        }
      }

lazy val flyway = (project in file("flyway"))
  .settings(flywaySettings:_*)
  .settings(
  scalaVersion := "2.11.6",
  flywayUrl := databaseUrl,
  flywayUser := databaseUser,
  flywayPassword := databasePassword,
  flywayLocations := Seq("filesystem:server/conf/db/migration/default")
)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
    , "com.github.japgolly.scalajs-react" %%% "core" % "0.9.2"
  ),
  jsDependencies ++= Seq(
    "org.webjars" % "react" % "0.12.2" / "react-with-addons.js" commonJSName "React"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val server = (project in file("server"))
  .settings(slickCodegenSettings:_*)
  .settings(scalariformSettings:_*)
  .settings(
    scalaJSProjects := clients,
    pipelineStages := Seq(scalaJSProd)
  ).
  enablePlugins(PlayScala)
  .settings(
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      jdbc,
      "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
      "com.typesafe.play" %% "play-slick" % "1.0.1",
      "com.typesafe.slick" %% "slick" % "3.0.3",
      "joda-time" % "joda-time" % "2.7",
      "org.joda" % "joda-convert" % "1.7",
      "com.github.tototoshi" %% "slick-joda-mapper" % "2.0.0",
      "com.h2database" % "h2" % "1.4.186"
    ),
    slickCodegenDatabaseUrl := databaseUrl,
    slickCodegenDatabaseUser := databaseUser,
    slickCodegenDatabasePassword := databasePassword,
    slickCodegenDriver := codegenDriver,
    slickCodegenJdbcDriver := jdbcDriver,
    slickCodegenOutputPackage := "models",
    slickCodegenExcludedTables := Seq("schema_version"),
    slickCodegenCodeGenerator := serverSourceGenerator,
    sourceGenerators in Compile <+= slickCodegen
).enablePlugins(PlayScala)
.aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(slickCodegenSettings:_*).
  settings(
    scalaVersion := scalaV,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "upickle" % "0.3.6"
    ),
    slickCodegenDatabaseUrl := databaseUrl,
    slickCodegenDatabaseUser := databaseUser,
    slickCodegenDatabasePassword := databasePassword,
    slickCodegenDriver := codegenDriver,
    slickCodegenJdbcDriver := jdbcDriver,
    slickCodegenOutputPackage := "models",
    slickCodegenExcludedTables := Seq("schema_version"),
    slickCodegenCodeGenerator := sharedSourceGenerator,
    sourceGenerators in Compile <+= slickCodegen
).
jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
