package com.orange.sophia.route

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.orange.sophia.route.actor.ServiceActor
import com.orange.sophia.route.api.Discover

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SophiaRoute extends Discover {

  override implicit def system: ActorSystem = ActorSystem("Sophia-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  override implicit def discoverActor: ActorRef = system.actorOf(ServiceActor.props, "servicesActor")


  lazy val routes: Route = discoverRoute

  def main(args: Array[String]): Unit = {

    Http().bindAndHandle(routes, "localhost", 8080)

    println("server started...")

    Await.result(system.whenTerminated, Duration.Inf)
  }

}
