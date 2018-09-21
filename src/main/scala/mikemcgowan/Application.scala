package mikemcgowan

import org.querki.jquery._
import org.scalajs.dom.XMLHttpRequest

object Application {

  type Matrix = Array[Array[String]]

  private val url =
    "https://raw.githubusercontent.com/mikemcgowan/memrise-scraper-scala/master/memrise_database.csv"

  def main(args: Array[String]): Unit =
    $(() => init())

  def setStatus(t: String): Unit =
    $("#status") text t

  def init(): Unit = {
    $("""<div id="status"></div>""").appendTo($("body"))
    setStatus("Please wait while the CSV file is downloaded ...")
    downloadCsv()
  }

  def downloadCsv(): Unit = {
    val xhr = new XMLHttpRequest()
    xhr.open("GET", url)
    xhr.onload = _ =>
     if (xhr.status == 200) {
        setStatus("Please wait while the CSV file is parsed ...")
        val data = parseCsv(xhr.responseText)
        data foreach (d => println("%s means %s" format(d(0), d(1))))
        setStatus("Found %d vocab items!" format data.length)
      } else {
        setStatus("Couldn't download CSV file.")
      }
    xhr.send()
  }

  def parseCsv(raw: String): Matrix = {
    val lines = raw split "\r\n"
    lines.tail map (_.split(','))
  }

}
