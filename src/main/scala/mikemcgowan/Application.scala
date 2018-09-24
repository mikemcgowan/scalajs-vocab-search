package mikemcgowan

import org.scalajs.dom.document

object Application {

  def main(args: Array[String]): Unit = {
    components.App.Component().renderIntoDOM(
      document.getElementById("app")
    )
  }

}
