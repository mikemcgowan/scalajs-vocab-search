package mikemcgowan.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import mikemcgowan.Downloader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object App {

  type VocabItem = (String, String, Option[String], String)
  type Vocab = List[VocabItem]

  private val minTermLength = 3
  private val maxMatches = 10

  case class State(
    searchDisabled: Boolean,
    searchTerm: String,
    statusText: String,
    fullVocab: Option[Vocab],
    filteredVocab: Option[Vocab]
  )

  private val initialState = State(
    searchDisabled = true,
    searchTerm = "",
    statusText = "Please wait while the CSV file is downloaded and parsed ...",
    None,
    None
  )

  val Component = ScalaComponent.builder[Unit]("Application")
    .initialState(initialState)
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

  class Backend($: BackendScope[Unit, State]) {

    def downloadVocab: Future[Callback] = Downloader.download.map { vocab =>
      $.modState(_.copy(
        searchDisabled = false,
        statusText = "%d vocab items" format vocab.length,
        fullVocab = Some(vocab)
      ))
    }

    def start: Callback =
      Callback future downloadVocab

    def setSearchTerm(searchTerm: String): Callback =
      $.modState(state => {
        val searchResults = performSearch(state.fullVocab.getOrElse(Nil), searchTerm)

        val nextFilteredVocab =
          if (searchTerm.length < minTermLength)
            None
          else
            Some(searchResults.take(maxMatches))

        val nextStatusText =
          if (nextFilteredVocab.isDefined)
            "Showing %s%d item%s matching \"%s\"" format (
              if (searchResults.size > maxMatches) "first %d of " format maxMatches else "",
              searchResults.size,
              if (nextFilteredVocab.get.size == 1) "" else "s",
              searchTerm
            )
          else
            "%d vocab items" format state.fullVocab.get.length

        state.copy(
          searchTerm = searchTerm,
          filteredVocab = nextFilteredVocab,
          statusText = nextStatusText
        )
      })

    def performSearch(vocab: Vocab, term: String): Vocab =
      vocab filter {
        case (a, b, None, d)    => List(a, b, d)    exists (_.toLowerCase contains term.toLowerCase)
        case (a, b, Some(c), d) => List(a, b, c, d) exists (_.toLowerCase contains term.toLowerCase)
      }

    def render(s: State) =
      <.div(^.cls := "container",
        <.h1("Scala.js vocab search"),
        <.div(^.cls := "row",
          <.div(^.cls := "column",
            Search.Component(Search.Props(s.searchDisabled, s.searchTerm, setSearchTerm))
          ),
        ),
        <.div(^.cls := "row",
          <.div(^.cls := "column",
            Status.Component(s.statusText)
          ),
        ),
        Table.Component(Table.Props(s.filteredVocab, s.searchTerm))
      )

  }

}