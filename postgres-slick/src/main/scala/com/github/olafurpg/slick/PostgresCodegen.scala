package com.github.olafurpg.slick

import java.io.File

import slick.codegen.SourceCodeGenerator
import slick.jdbc.JdbcBackend
import slick.profile.SqlProfile.ColumnOption
import slick.{ model => m }
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

object Codegen extends PostgresCodegen {
  val slickDriver = PostgresDriver
  val jdbcProfile = "org.postgresql.Driver"
  val url = "jdbc:postgresql://192.168.59.103:5432/postgres"
  val user = "postgres"
  val password = "secret"
  lazy val sharedSourceGenerator = (model: m.Model) => sourceGenerator(model, true)
  lazy val serverSourceGenerator = (model: m.Model) => sourceGenerator(model, false)
  val excluded = Seq("schema_version")
  val configs = Seq(
    Config(serverSourceGenerator, "server/app", "models", "Tables.scala", "Tables")
  //    , Config(sharedSourceGenerator, "shared/src/main/scala", "models", "Tables.scala", "Tables")
  )
  def main(args: Array[String]) {
    println("hello from github!")
    gen(
      slickDriver,
      jdbcProfile,
      url,
      user,
      password,
      configs,
      excluded
    )
  }
}

case class Config(
  generator: m.Model => SourceCodeGenerator,
  outputDir: String,
  pkg: String,
  fileName: String,
  container: String)

trait PostgresCodegen  extends JdbcBackend {
  val db = Database.forConfig("mydb")
  def gen(driver: JdbcProfile,
    jdbcDriver: String,
    url: String,
    user: String,
    password: String,
    configs: Seq[Config],
    excluded: Seq[String]): Seq[File] = {

    val database = driver.api.Database.forURL(url = url, driver = jdbcDriver, user = user, password = password)

    try {
      database.source.createConnection().close()
    }
    catch {
      case e: Throwable =>
        throw new RuntimeException("Failed to run slick-codegen: " + e.getMessage, e)
    }

    println(s"Generate source code with slick-codegen: url=${url}, user=${user}")
    val tables = driver.defaultTables.map(ts => ts.filterNot(t => excluded contains t.name.name))

    configs.map { config =>
      val dbio = for {
        m <- driver.createModel(Some(tables))
      } yield config.generator(m).writeToFile(
        profile = driver.getClass.getCanonicalName.stripSuffix("$"),
        folder = config.outputDir,
        pkg = config.pkg,
        container = config.container,
        fileName = config.fileName
      )
      database.run(dbio)

      val generatedFile = config.outputDir + "/" + config.pkg.replaceAllLiterally(".", "/") + "/" + config.fileName
      println(s"Source code has generated in $generatedFile")
      new File(generatedFile)
    }
  }
  lazy val sourceGenerator = (model: m.Model, shared: Boolean) =>
    new SourceCodeGenerator(model) {
      override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]): String = {
        s"""
package ${pkg}
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait ${container}${parentType.map(t => s" extends $t").getOrElse("")} {
  val profile = $profile
  import profile.api._
  ${indent(code)}
}
      """.trim()
      }
      override def code = {
        if (shared)
          tables.map(_.code.mkString("\n")).mkString("\n\n")
        else
          super.code
      }
      override def tableName = (dbName: String) => super.tableName(dbName) + "Table"
      //      override def entityName = (dbName: String) => dbName
      // disable entity class generation and mapping
      override def Table = new Table(_) {
        //        override def EntityType = new EntityType {
        //          override def doc = if (!shared) "" else super.doc
        //          override def code = if (!shared) "" else super.code
        //        }
        //        override def PlainSqlMapper = new PlainSqlMapper {
        //          override def doc = if (shared) "" else super.doc
        //          override def code = if (shared) "" else super.code
        //        }
        //        override def TableClass = new TableClass {
        //          override def doc = if (shared) "" else super.doc
        //          override def code = if (shared) "" else super.code
        //        }
        //        override def TableValue = new TableValue {
        //          override def doc = if (shared) "" else super.doc
        //          override def code = if (shared) "" else super.code
        //        }
        override def Column = new Column(_) {
          override def rawType = model.tpe match {
            case "java.sql.Timestamp" => "com.geirsson.util.Epoch"
            case "String" => model.options.find(_.isInstanceOf[ColumnOption.SqlType])
              .map(_.asInstanceOf[ColumnOption.SqlType].typeName).map({
                case "_text" => "List[String]"
                case x => {
                  //                println(s"Can't map ${model.name} $x")
                  "String"
                }
              }).getOrElse("String")
            case _ => super.rawType
          }
          override def rawName: String = model.name
        }
      }
    }

}
