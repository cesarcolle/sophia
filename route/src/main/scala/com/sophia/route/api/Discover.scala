package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.orange.sophia.route.actor.ServiceActor
import com.orange.sophia.route.marshall.JsonSupport

import scala.concurrent.duration._


trait Discover extends JsonSupport {

  import com.orange.sophia.route.actor.ServiceActor._

  implicit def system: ActorSystem

  val discoverActor: ActorRef = system.actorOf(Props[ServiceActor])

  private val actorMaterializer = ActorMaterializer

  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration


  val discoverRoute: Route = concat(
    path("list") {
      get {
        val services = (discoverActor ? GetServices).mapTo[Services]
        logRequest("asking for list of micro-service...")
        complete(services)
      }
    },
    path("addService") {
      post {
        entity(as[AddService]) { service =>
          val serviceAdded = ask(discoverActor, service).mapTo[ServiceActionPerformed]
          complete(serviceAdded)
        }
      }
    },
    path("getService") {
      get {
        parameter('nameService) { nameService =>
          val servicesDescription = (discoverActor ? GetServiceByName(nameService)).mapTo[NamedServices]
          complete(servicesDescription)
        }
      }
    }

  )

}
