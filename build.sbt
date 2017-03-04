val commonSettings = Seq(
  organization := "com.github.karasiq",
  version := "1.0.0-SNAPSHOT",
  isSnapshot := version.value.endsWith("SNAPSHOT"),
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1"),
  licenses := Seq("Apache License, Version 2.0" → url("http://opensource.org/licenses/Apache-2.0"))
)

// -----------------------------------------------------------------------
// Shared
// -----------------------------------------------------------------------
lazy val model = crossProject
  .crossType(CrossType.Pure)
  .settings(commonSettings)
  .jvmSettings(libraryDependencies ++= ProjectDeps.akka.actors)
  .jsSettings(ScalaJsDeps.akka.actors)

lazy val modelJVM = model.jvm

lazy val modelJS = model.js

// -----------------------------------------------------------------------
// Core
// -----------------------------------------------------------------------
lazy val core = project
  .settings(commonSettings)
  .dependsOn(modelJVM, bouncyCastleCrypto)

// -----------------------------------------------------------------------
// Plugins
// -----------------------------------------------------------------------
lazy val bouncyCastleCrypto = (project in file("crypto-bc"))
  .settings(commonSettings)

// -----------------------------------------------------------------------
// HTTP
// -----------------------------------------------------------------------
lazy val server = project
  .settings(commonSettings)
  .settings(
    scalaJsBundlerAssets in Compile += {
      import com.karasiq.scalajsbundler.dsl._
      Bundle("index", WebDeps.bootstrap, WebDeps.indexHtml, scalaJsApplication(webapp, fastOpt = true).value)
    },
    scalaJsBundlerCompile in Compile <<= (scalaJsBundlerCompile in Compile)
      .dependsOn(fastOptJS in Compile in webapp)
  )
  .dependsOn(core)
  .enablePlugins(ScalaJSBundlerPlugin, JavaAppPackaging)

lazy val webapp = project
  .settings(commonSettings)
  .enablePlugins(ScalaJSPlugin)

// -----------------------------------------------------------------------
// Misc
// -----------------------------------------------------------------------
lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "shadowcloud-shell",
    mainClass in Compile := Some("com.karasiq.shadowcloud.test.Benchmark")
  )
  .dependsOn(core)