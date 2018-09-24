package mikemcgowan.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Search {

  case class Props(
    searchDisabled: Boolean,
    searchTerm: String,
    setSearchTerm: String => Callback
  )

  private def onTextChange(f: String => Callback)(e: ReactEventFromInput): Callback =
    f(e.target.value)

  val Component = ScalaComponent.builder[Props]("Search")
    .render_P(props =>
      <.input(
        ^.`type` := "text",
        ^.placeholder := "Search",
        ^.disabled := props.searchDisabled,
        ^.value := props.searchTerm,
        ^.onChange ==> onTextChange(props.setSearchTerm)
      )
    )
    .build

}
