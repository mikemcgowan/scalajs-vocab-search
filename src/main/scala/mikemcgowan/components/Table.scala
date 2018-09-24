package mikemcgowan.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import mikemcgowan.components.App.Vocab

object Table {

  case class Props(
    vocab: Option[Vocab],
    searchTerm: String
  )

  private val columnHeaders = List(
    "Polish",
    "English",
    "Other",
    "Type"
  )

  private def hilight(s: String, term: String) =
    if (s.toLowerCase contains term.toLowerCase)
      <.td(
        ^.dangerouslySetInnerHtml := (s replace (term, "<strong>%s</strong>" format term))
      )
    else
      <.td(s)

  private def render(props: Props) = {
    if (props.vocab.isDefined && props.vocab.get.nonEmpty)
      <.table(
        <.thead(
          <.tr((columnHeaders map (s =>
            <.th(^.key := s, s)
            )).toVdomArray)
        ),
        <.tbody((props.vocab.get.zipWithIndex map {
          case (vocabItem, index) =>
            <.tr(^.key := index,
              hilight(vocabItem._1, props.searchTerm),
              hilight(vocabItem._2, props.searchTerm),
              hilight(vocabItem._3 getOrElse "n/a", props.searchTerm),
              hilight(vocabItem._4, props.searchTerm)
            )
        }).toVdomArray)
      )
    else
      <.div()
  }

  val Component = ScalaComponent.builder[Props]("Table")
    .render_P(render)
    .build

}
