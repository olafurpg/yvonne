import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

import japgolly.scalajs.react._
import models.User
import org.scalajs.dom
import scala.scalajs.js.JSApp
import dom.document
import scala.scalajs.js.annotation.JSExport


object ReactApp extends JSApp {
  val user = User(0, "Olafur Geirsson", 0L)

  val NoArgs =
    ReactComponentB[Unit]("No args")
      .render(_ => <.div(s"User name: ${user.name}"))
      .buildU

  @JSExport
  override def main(): Unit = {
    React.render(NoArgs(), document.body)
  }

}
