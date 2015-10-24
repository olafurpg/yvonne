package controllers

import javax.inject.Inject

import autowire.Core.Request
import com.geirsson.util.HttpError
import com.geirsson.util.Unauthorized
import models.UserDAO
import play.api.http.Status
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Result
import play.api.mvc.Results
import services.MyApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.MySecondApi
import scala.concurrent.Future
import upickle.default.Reader
import upickle.default.Writer

import scala.util.Try


case class User(scopes: List[String])

class MyApiImpl(user: User) extends MyApi {
  require(user.scopes.contains("admin"))
  override def doThing(i: Int, j: Int): Int = {
    println(user)
    i * j
  }

  override def gimmeStrings(i: Int, str: String): Seq[String] = Seq.fill(i)(str)
}

object MySecondApiImpl extends MySecondApi {
  override def doThing2(i: Int, j: Int): Future[Int] = {
    println("Second API")
    throw Unauthorized
//    i + j
  }

  override def gimmeStrings2(i: Int, str: String): Seq[String] = Seq.fill(i)(str)
}

class MyServer(user: User) extends autowire.Server[String, Reader, Writer] {
  def write[AutowireResult: Writer](r: AutowireResult) = upickle.default.write(r)
  def read[AutowireResult: Reader](p: String) = upickle.default.read[AutowireResult](p)

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

  def handleError: PartialFunction[Throwable, Result] = {
    case HttpError(status) => new Status(status)
    case e: IllegalArgumentException => BadRequest(e.getMessage)
    case autowire.Error.InvalidInput(exs) => BadRequest(exs.toString)
  }

  def api = Action.async { implicit request =>
    try {
      val user = User(List("admin"))
      val req = upickle.default.read[Request[String]](request.body.asText.get)
      println(req.path)
      val result = new MyServer(user).routes(req)
      result.map { txt =>
        println(txt)
        Ok(txt)
      }
    } catch {
      case e: Throwable => Future.successful(handleError(e))
    }
  }
}
