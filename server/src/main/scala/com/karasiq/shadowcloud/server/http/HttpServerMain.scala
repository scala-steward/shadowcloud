package com.karasiq.shadowcloud.server.http

import java.nio.file.{Files, Paths}

import scala.language.postfixOps

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.PredefinedToResponseMarshallers
import akka.http.scaladsl.server._
import akka.http.scaladsl.settings.ServerSettings

import com.karasiq.shadowcloud.ShadowCloud
import com.karasiq.shadowcloud.storage.props.StorageProps

object HttpServerMain extends HttpApp with App with PredefinedToResponseMarshallers with SCAkkaHttpServer {
  // Actor system
  private[this] val actorSystem: ActorSystem = ActorSystem("shadowcloud-server")
  protected val sc = ShadowCloud(actorSystem)

  import sc.implicits._
  import sc.ops.supervisor

  // -----------------------------------------------------------------------
  // Route
  // -----------------------------------------------------------------------
  protected val routes: Route = {
    scRoute ~ staticFilesRoute
  }

  private[this] def staticFilesRoute: Route = {
    encodeResponse {
      pathEndOrSingleSlash {
        getFromResource("webapp/index.html")
      } ~ {
        getFromResourceDirectory("webapp")
      }
    }
  }

  // -----------------------------------------------------------------------
  // Pre-start
  // -----------------------------------------------------------------------
  sc.keys.getOrGenerateChain().foreach { keyChain ⇒
    println(s"Key chain initialized: $keyChain")
  }

  val tempDirectory = sys.props.get("shadowcloud.temp-storage-dir")
    .map(Paths.get(_))
    .getOrElse(Files.createTempDirectory("scl-temp-storage"))

  Files.createDirectories(tempDirectory.resolve("second"))
  supervisor.addRegion("testRegion", sc.configs.regionConfig("testRegion"))
  supervisor.addStorage("testStorage", StorageProps.fromDirectory(tempDirectory))
  supervisor.addStorage("testStorage2", StorageProps.fromDirectory(tempDirectory.resolve("second")))
  supervisor.register("testRegion", "testStorage")
  supervisor.register("testRegion", "testStorage2")

  import scala.concurrent.duration._
  actorSystem.scheduler.scheduleOnce(30 seconds) {
    sc.ops.region.collectGarbage("testRegion", delete = false)
  }

  // -----------------------------------------------------------------------
  // Start server
  // -----------------------------------------------------------------------
  startServer("0.0.0.0", 9000, ServerSettings(actorSystem), actorSystem)
}
