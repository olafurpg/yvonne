import sbt.File
import sbt.Keys.TaskStreams
import slick.codegen.SourceCodeGenerator
import slick.{ model => m }
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

object SlickCodegen extends SlickCodegen

trait SlickCodegen {
  case class Config(
    generator: m.Model => SourceCodeGenerator,
    outputDir: String,
    pkg: String,
    fileName: String,
    container: String)
  def apply(
           driver: JdbcProfile,
           jdbcDriver: String,
           url: String,
           user: String,
           password: String,
           configs: Seq[SlickCodegen.Config],
           excluded: Seq[String],
           s: TaskStreams): Seq[File] = {

    val database = driver.api.Database.forURL(url = url, driver = jdbcDriver, user = user, password = password)

    try {
      database.source.createConnection().close()
    } catch {
      case e: Throwable =>
        throw new RuntimeException("Failed to run slick-codegen: " + e.getMessage, e)
    }

    s.log.info(s"Generate source code with slick-codegen: url=${url}, user=${user}")
    val tables = driver.defaultTables.map(ts => ts.filterNot(t => excluded contains t.name.name))

    configs.map { config =>
      val dbio = for {
        m <- driver.createModel(Some(tables))
      } yield config.generator(m).writeToFile(
          profile = "slick.driver." + driver.toString,
          folder = config.outputDir,
          pkg = config.pkg,
          container = config.container,
          fileName = config.fileName
        )
      database.run(dbio)

      val generatedFile = config.outputDir + "/" + config.pkg.replaceAllLiterally(".", "/") + "/" + config.fileName
      s.log.info(s"Source code has generated in $generatedFile")
      new File(generatedFile)
    }
  }
  lazy val sourceGenerator = (model:  m.Model, shared: Boolean) =>
    new SourceCodeGenerator(model) {
      override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]) : String = {
        if (shared) s"""
                       |package ${pkg}
            |
            |import com.geirsson.util.Epoch
            |
            |$code
         """.stripMargin
        else super.packageCode(profile, pkg, container, parentType)
      }
      override def code = {
        if (shared)
          tables.map(_.code.mkString("\n")).mkString("\n\n")
        else
          s"""
             |import com.geirsson.util.Epoch
             |implicit val dateTimeMapper =  MappedColumnType.base[Epoch, java.sql.Timestamp](
             | { epoch =>  new java.sql.Timestamp(epoch.millis) },
             | { ts    =>  Epoch(ts.getTime()) }
             |)
             |""".stripMargin + super.code
      }
      override def tableName = (dbName: String) => dbName + "Table"
      override def entityName = (dbName: String) => dbName
      // disable entity class generation and mapping
      override def Table = new Table(_) {
        override def EntityType = new EntityType{
          override def doc = if (!shared) "" else super.doc
          override def code = if (!shared) "" else super.code
        }
        override def PlainSqlMapper = new PlainSqlMapper {
          override def doc = if (shared) "" else super.doc
          override def code = if (shared) "" else super.code
        }
        override def TableClass = new TableClass {
          override def doc = if (shared) "" else super.doc
          override def code = if (shared) "" else super.code
        }
        override def TableValue = new TableValue {
          override def doc = if (shared) "" else super.doc
          override def code = if (shared) "" else super.code
        }
        override def Column = new Column(_) {
          override def rawType = model.tpe match {
            case "java.sql.Timestamp" => "Epoch" // kill j.s.Timestamp
            case _ => super.rawType
          }
          override def rawName: String = model.name
        }
      }
    }

}
