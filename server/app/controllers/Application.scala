package controllers

import com.geirsson
import org.joda.time.DateTime
import models.Tables
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import upickle.default._

object Application extends Controller with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._
  /**
   * Entity class storing rows of table User
   *  @param id Database column id SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(32,true)
   *  @param createdAt Database column createdAt SqlType(TIMESTAMP)
   */
  case class UserRow(id: Int, name: String, createdAt: DateTime)
  object UserRow {
    implicit val jsonFormat = Json.format[UserRow]
  }

  def index = Action { implicit request =>
    val user = Tables.UserRow(0, "user", DateTime.now.getMillis)
    Ok(write(user))
  }
}

object Implicits {
}
