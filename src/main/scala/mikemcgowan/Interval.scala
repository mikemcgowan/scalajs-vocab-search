package mikemcgowan

import org.scalajs.dom.window.{setInterval, clearInterval}

class Interval {

  private var interval: Option[Int] = None

  def set(f: () => Unit, timeout: Int): Unit = {
    interval flatMap Interval.clear
    interval = Some(setInterval(f, timeout))
  }

  def clear(): Unit = {
    interval flatMap Interval.clear
    interval = None
  }

}

object Interval {

  private def clear(i: Int): Option[Int] = {
    clearInterval(i)
    None
  }

}
