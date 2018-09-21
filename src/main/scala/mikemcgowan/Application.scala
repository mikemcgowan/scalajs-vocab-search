package mikemcgowan

import org.querki.jquery._
import org.scalajs.dom.Event

object Application {

  type VocabItem = (String, String, Option[String], String)
  type Vocab = List[VocabItem]

  val searchAfterMillis = 500
  val minTermLength = 3
  val maxMatches = 10
  val interval = new Interval()

  def main(args: Array[String]): Unit =
    $(() => init())

  def setStatus(t: String): Unit =
    $("#status") text t

  def init(): Unit = {
    $("#table").hide()
    $("""<input type="text" placeholder="Search" id="search" disabled="disabled"/>""")
      .appendTo($("#row1"))
    $("""<small id="status"/>""")
      .appendTo($("#row2"))
    setStatus("Please wait while the CSV file is downloaded and parsed ...")
    Downloader download {
      case Right(data) => initSearch(data)
      case Left(e)     => setStatus(e)
    }
  }

  def initSearch(vocab: Vocab): Unit = {
    setStatus("%d vocab items" format vocab.length)
    val s = $("#search")
    s removeAttr "disabled"
    s.focus()
    s.keyup((_: Event) => interval.set(() => doSearch(vocab), searchAfterMillis))
  }

  def doSearch(vocab: Vocab): Unit = {
    interval.clear()
    val term = $("#search").value().asInstanceOf[String]
    if (term.length >= minTermLength) {
      setStatus("Searching for \"%s\" ..." format term)
      val matches = performSearch(vocab, term)
      val first =
        if (matches.size > maxMatches)
          "first %d of " format maxMatches
        else
          ""
      setStatus("Showing %s%d item%s matching \"%s\"" format (
        first,
        matches.size,
        if (matches.size == 1) "" else "s",
        term
      ))
      renderVocab(matches take maxMatches, term)
    } else {
      setStatus("%d vocab items" format vocab.length)
      $("#tbody").empty()
      $("#table").hide()
    }
  }

  def performSearch(vocab: Vocab, term: String): List[VocabItem] =
    vocab filter {
      case (a, b, None, d)    => List(a, b, d)    exists (_.toLowerCase contains term.toLowerCase)
      case (a, b, Some(c), d) => List(a, b, c, d) exists (_.toLowerCase contains term.toLowerCase)
    }

  def renderVocab(vocab: Vocab, term: String): Unit = {
    $("#tbody").empty()
    if (vocab.isEmpty)
      $("#table").hide()
    else {
      $("#table").show()
      vocab foreach ((item: VocabItem) => {
        $("""<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>""" format (
          hilight(item._1, term),
          hilight(item._2, term),
          hilight(item._3.getOrElse("n/a"), term),
          hilight(item._4, term)
        )).appendTo($("#tbody"))
      })
    }
  }

  def hilight(s: String, term: String): String =
    s replace (term, "<strong>%s</strong>" format term)

}
