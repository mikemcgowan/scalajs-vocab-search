package mikemcgowan

import kantan.csv.ops._
import mikemcgowan.Application.{Vocab, VocabItem}
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Downloader {

  private val csvUrl =
    "https://raw.githubusercontent.com/mikemcgowan/memrise-scraper-scala/master/memrise_database.csv"

  def download: Future[Vocab] =
    Ajax.get(csvUrl) map { xhr =>
      parseCsv(xhr.responseText)
    }

  private def parseCsv(raw: String): Vocab = {
    val reader = raw.asCsvReader[VocabItem](',', header = true)
    val buf = scala.collection.mutable.ListBuffer.empty[VocabItem]
    reader foreach {
      case Right(x) => buf += x
      case _ => ()
    }
    buf.toList
  }

}
