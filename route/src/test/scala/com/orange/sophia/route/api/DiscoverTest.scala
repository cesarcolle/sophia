package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.ServiceActor
import com.orange.sophia.route.actor.ServiceActor.AddService
import org.scalatest.{FunSuite, Matchers, WordSpecLike}
import org.scalatest.concurrent.ScalaFutures

class DiscoverTest extends  WordSpecLike  with ScalaFutures  with  Matchers with ScalatestRouteTest with Discover {

  val routes: Route = discoverRoute
  override def discoverActor: ActorRef = system.actorOf(ServiceActor.props)


  "listing empty services" should  {
    "list the service " in {
      val request = HttpRequest(uri = "/list")
      request ~> routes ~> check {
        status === StatusCodes.OK
        entityAs[String] === "{services:[]}"
      }
    }
  }

  "Add service test" should {
    "return the proper message for a service added" in {
      val addservice = AddService("hey", "ho", 12)
      val addServiceEntity = Marshal(addservice).to[MessageEntity].futureValue
      val request = Post("/addService").withEntity(addServiceEntity)
      request ~> routes ~> check {
        status === StatusCodes.OK
        entityAs[String] === "{message:service added.}"
      }
    }
  }


}
