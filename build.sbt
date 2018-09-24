enablePlugins(ScalaJSPlugin)

name := "Scala.js vocab search"
scalaVersion := "2.12.6"

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"
libraryDependencies += "com.nrinaudo" %%% "kantan.csv" % "0.4.0"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.3.1"

skip in packageJSDependencies := false
dependencyOverrides += "org.webjars.npm" % "js-tokens" % "3.0.2"
jsDependencies ++= Seq(
  "org.webjars.npm" % "react" % "16.5.1"
    /        "umd/react.development.js"
    minified "umd/react.production.min.js"
    commonJSName "React",

  "org.webjars.npm" % "react-dom" % "16.5.1"
    /         "umd/react-dom.development.js"
    minified  "umd/react-dom.production.min.js"
    dependsOn "umd/react.development.js"
    commonJSName "ReactDOM",

  "org.webjars.npm" % "react-dom" % "16.5.1"
    /         "umd/react-dom-server.browser.development.js"
    minified  "umd/react-dom-server.browser.production.min.js"
    dependsOn "umd/react-dom.development.js"
    commonJSName "ReactDOMServer"
)

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
