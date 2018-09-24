package mikemcgowan.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Status {

  val Component = ScalaComponent.builder[String]("Status")
    .render_P(s => <.small(s))
    .build

}
