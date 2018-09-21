package mikemcgowan

import mikemcgowan.Application.{Vocab, VocabItem}
import kantan.csv.ops._
import org.scalajs.dom.XMLHttpRequest

object Downloader {

  val csvUrl = "https://raw.githubusercontent.com/mikemcgowan/memrise-scraper-scala/master/memrise_database.csv"

  def download(callback: Either[String, Vocab] => Unit): Unit = {
    val xhr = new XMLHttpRequest()
    xhr.open("GET", csvUrl)
    xhr.onload = _ =>
      xhr.status match {
        case 200 => callback(Right(parseCsv(xhr.responseText)))
        case _   => callback(Left("Couldn't download CSV file."))
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
