package controllers

import javax.inject.Inject

import autowire.Core.Request
import models.DAO
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.mvc.Action
import play.api.mvc.Controller
import services.MyApi
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play
import upickle._
import upickle.default.Reader
import upickle.default.Writer

case object NotFoundError extends RuntimeException

object MyApiImpl extends MyApi {
  def doThing(i: Int, j: Int): Int = i * j

  override def gimmeStrings(i: Int, str: String): Seq[String] = Seq.fill(i)(str)
}

object MyServer extends autowire.Server[String, Reader, Writer] {
  def write[Result: Writer](r: Result) = upickle.default.write(r)
  def read[Result: Reader](p: String) = upickle.default.read[Result](p)

  val routes = MyServer.route[MyApi](MyApiImpl)
}

class Application extends Controller with DAO {
  import profile.api._

  def index = Action.async { implicit request =>
      db.run(AppUserTable.result).map { result =>
        println(result)
        Ok(views.html.main())
      }
  }

  def api = Action.async { implicit request =>
    try {
      val req = upickle.default.read[Request[String]](request.body.asText.get)
      val result = MyServer.routes(req)
      result.map { txt =>
        Ok(txt)
      }
    } catch {
      case autowire.Error.InvalidInput(exs) =>
        println(exs)
        Future.successful(BadRequest)
    }
  }
}
