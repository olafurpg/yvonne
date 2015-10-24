import com.geirsson.util.UnauthorizedError
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

import japgolly.scalajs.react._
import org.scalajs.dom
import org.scalajs.dom.ext.AjaxException
import services.AdminApi
import services.MyApi
import services.MySecondApi
import scala.scalajs.js.JSApp
import dom.document
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.ExecutionContext.Implicits.global
import autowire._
import upickle.default._

// client-side implementation, and call-site
object MyClient extends autowire.Client[String, Reader, Writer] {
  def write[Result: Writer](r: Result): String = upickle.default.write(r)
  def read[Result: Reader](p: String): Result = upickle.default.read[Result](p)

  override def doCall(req: Request) = {
    dom.ext.Ajax.post(
      url = "/autowire",
      data = upickle.default.write(req)
    ).map(_.responseText)
  }
}

object ReactApp extends JSApp {

  val NoArgs =
    ReactComponentB[Unit]("NoArgs")
      .render(_ => <.div(s"User name: Olafur Pall"))
      .buildU

  @JSExport
  override def main(): Unit = {
    val result = MyClient[AdminApi].doSecretThing("Am I an admin?").call()
    result.map { result =>
      println(result)
    }.recover {
      case AjaxException(xhr) if xhr.status == UnauthorizedError.status => {
        println("You are not an admin.")
      }
    }

    React.render(NoArgs(), document.body)
  }

}
