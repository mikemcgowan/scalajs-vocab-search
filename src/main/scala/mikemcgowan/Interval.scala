package mikemcgowan

import org.scalajs.dom.window.{setInterval, clearInterval}

class Interval {

  private var interval: Option[Int] = None

  def set(f: () => Unit, timeout: Int): Unit = {
    clear()
    interval = Some(setInterval(f, timeout))
  }

  def clear(): Unit = {
    if (interval.isDefined)
      clearInterval(interval.get)
    interval = None
  }

}
