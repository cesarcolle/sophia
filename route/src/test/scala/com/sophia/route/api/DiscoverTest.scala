package com.orange.sophia.route.api

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.orange.sophia.route.actor.ServiceActor.AddService
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.concurrent.ScalaFutures

class DiscoverTest extends FunSuite with Discover with Matchers with ScalatestRouteTest with ScalaFutures {

  lazy val routes = discoverRoute

  test("listing empty services") {
    val request = HttpRequest(uri = "/list")
    request ~> routes ~> check {
      status === StatusCodes.OK
      entityAs[String] === "{services:[]}"
    }
  }

  test("Add service test") {
    val addservice = AddService("hey", "ho", 12)
    val addServiceEntity = Marshal(addservice).to[MessageEntity].futureValue
    val request = Post("/addService").withEntity(addServiceEntity)
    request ~> routes ~> check {
      status === StatusCodes.OK
      entityAs[String] === "{message:service added.}"
    }
  }


}
