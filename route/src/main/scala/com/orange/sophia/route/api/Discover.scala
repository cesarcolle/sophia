package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.orange.sophia.route.actor.{JsonSupport, Service, ServiceActor}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask


trait Discover extends JsonSupport {

  import com.orange.sophia.route.actor.ServiceActor._

  implicit def system: ActorSystem

  def discoverActor: ActorRef = system.actorOf(Props[ServiceActor])

  private val actorMaterializer = ActorMaterializer

  case class ListService()

  val discoverRoute: Route = concat(
    path("list") {
      get {
        val services = ask(discoverActor, GetServices).mapTo[DiscoverServiceResult]
        logRequest("asking for list of micro-service...")
        complete(HttpResponse(entity = services).withStatus(StatusCodes.OK))
      }
    },
    path("addService") {
      post {
        entity(as[AddService]) { service =>
          val serviceAdded = ask(discoverActor, service).mapTo[ServiceActionPerformed]
          complete(discoverMarshal(serviceAdded))
        }
      }
    }
  )

}
