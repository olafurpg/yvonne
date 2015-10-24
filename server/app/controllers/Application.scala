package controllers

import javax.inject.Inject

import autowire.Core.Request
import models.UserDAO
import play.api.mvc.Action
import play.api.mvc.Controller
import services.MyApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.MySecondApi
import scala.concurrent.Future
import upickle.default.Reader
import upickle.default.Writer

case object NotFoundError extends RuntimeException

case class User(scopes: List[String])

class MyApiImpl(user: User) extends MyApi {
  require(user.scopes.contains("admin"))
  def doThing(i: Int, j: Int): Int = {
    println(user)
    i * j
  }

  override def gimmeStrings(i: Int, str: String): Seq[String] = Seq.fill(i)(str)
}

object MySecondApiImpl extends MySecondApi {
  override def doThing2(i: Int, j: Int): Int = {
    println("Second API")
    i * j
  }

  override def gimmeStrings2(i: Int, str: String): Seq[String] = Seq.fill(i)(str)
}

class MyServer(user: User) extends autowire.Server[String, Reader, Writer] {
  def write[Result: Writer](r: Result) = upickle.default.write(r)
  def read[Result: Reader](p: String) = upickle.default.read[Result](p)

  val api1 = new MyServer(user).route[MyApi](new MyApiImpl(user))
  val api2 = new MyServer(user).route[MySecondApi](MySecondApiImpl)

  val routes = api1 orElse api2
}

class Application @Inject()(val userDao: UserDAO) extends Controller {

  def index = Action.async { implicit request =>
    userDao.users.map { users =>
      println(users)
      Ok(views.html.main())
    }
  }

  def api = Action.async { implicit request =>
    try {
      val user = User(List("admin"))
      val req = upickle.default.read[Request[String]](request.body.asText.get)
      println(req.path)
      val result = new MyServer(user).routes(req)
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
