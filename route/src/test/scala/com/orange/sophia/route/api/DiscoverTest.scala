package com.orange.sophia.route.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DiscoverTest extends FunSuite with Discover with Matchers with ScalatestRouteTest{

  test("registering an user") {
    val request = HttpRequest(uri = "/list")

    request ~> discoverRoute ~> check {
      status should be === StatusCodes.OK
    }
  }

}
