logLevel := Level.Warn

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.25")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.0" // Should go before Scala.js

// addSbtPlugin("com.github.sbtliquibase" % "sbt-liquibase" % "0.2.0") // TODO: https://github.com/sbtliquibase/sbt-liquibase-plugin/issues/17

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.22")

addSbtPlugin("com.github.karasiq" % "sbt-scalajs-bundler" % "1.2.1")

// addSbtPlugin("pl.project13.sbt" % "sbt-jol" % "0.1.2")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
