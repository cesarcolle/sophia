package com.orange.sophia.route

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.orange.sophia.route.api.{DataPush, Discover}
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SophiaRoute extends Discover with DataPush{

  override implicit def system: ActorSystem = ActorSystem("Sophia-system")

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  lazy val routes: Route = concat(discoverRoute ,routeDataPush)

  def main(args: Array[String]): Unit = {

    Http().bindAndHandle(routes, "localhost", 8080)

    println("server started... at localhost:" + 8080)

    Await.result(system.whenTerminated, Duration.Inf)
  }

}
