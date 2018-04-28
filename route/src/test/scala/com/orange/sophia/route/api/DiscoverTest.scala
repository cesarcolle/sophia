package com.orange.sophia.route.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.orange.sophia.route.actor.ServiceActor
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DiscoverTest extends FunSuite with Discover with Matchers with ScalatestRouteTest{

  lazy val routes = discoverRoute

  test("registering an user") {
    val request = HttpRequest(uri = "/list")

    request ~> routes ~> check {
      status === StatusCodes.OK
    }
  }

  override implicit def discoverActor: ActorRef = system.actorOf(ServiceActor.props)

}
