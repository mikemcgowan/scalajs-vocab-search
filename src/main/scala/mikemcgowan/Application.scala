package mikemcgowan

import kantan.csv.ops._
import org.querki.jquery._
import org.scalajs.dom.XMLHttpRequest

object Application {

  type VocabItem = (String, String, Option[String], String)
  type Vocab = List[VocabItem]

  val csvUrl =
    "https://raw.githubusercontent.com/mikemcgowan/memrise-scraper-scala/master/memrise_database.csv"

  def main(args: Array[String]): Unit =
    $(() => init())

  def setStatus(t: String): Unit =
    $("#status") text t

  def init(): Unit = {
    $("""<input type="text" placeholder="Search" id="search"/>""")
      .appendTo($("#row1"))
    $("""<small id="status"/>""")
      .appendTo($("#row2"))

    setStatus("Please wait while the CSV file is downloaded ...")
    downloadCsv()
  }

  def downloadCsv(): Unit = {
    val xhr = new XMLHttpRequest()
    xhr.open("GET", csvUrl)
    xhr.onload = _ =>
     if (xhr.status == 200) {
        setStatus("Please wait while the CSV file is parsed ...")
        val data = parseCsv(xhr.responseText)
        data foreach (d => println("%s means %s" format(d._1, d._2)))
        setStatus("%d vocab items" format data.length)
      } else {
        setStatus("Couldn't download CSV file.")
      }
    xhr.send()
  }

  def parseCsv(raw: String): Vocab = {
    val reader = raw.asCsvReader[VocabItem](',', header = true)
    val buf = scala.collection.mutable.ListBuffer.empty[VocabItem]
    reader foreach {
      case Right(x) => buf += x
      case _ => ()
    }
    buf.toList
  }

}
