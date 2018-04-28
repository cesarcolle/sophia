package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.duration._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.orange.sophia.route.actor._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout


trait Discover extends JsonSupport {

  import com.orange.sophia.route.actor.ServiceActor._

  implicit def system: ActorSystem

  implicit def discoverActor: ActorRef

  private val actorMaterializer = ActorMaterializer

  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  case class ListService()

  val discoverRoute: Route = concat(
    path("list") {
      get {
        val services = (discoverActor ? GetServices).mapTo[Services]
        logRequest("asking for list of micro-service...")
        complete("lol")
      }
    },
    path("addService") {
      post {
        entity(as[AddService]) { service =>
          val serviceAdded = ask(discoverActor, service).mapTo[ServiceActionPerformed]
          complete("lolilol")
        }
      }
    }
  )

}
