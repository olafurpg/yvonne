package controllers

import org.joda.time.DateTime
import models.{ Tables, User }
import models.Tables._
import play.api._
import play.api.mvc._
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import upickle.default._

object Application extends Controller with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val users = UserTable.returning(UserTable.map(_.id))

  def index = Action.async { implicit request =>
    Future {
      Ok(views.html.main())
    }
  }
}
