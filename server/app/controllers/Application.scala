package controllers

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play
import models.Tables._

class Application @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val users = UserTable.returning(UserTable.map(_.id))

  def index = Action.async { implicit request =>
    Play.current
    Future {
      Ok(views.html.main())
    }
  }
}
