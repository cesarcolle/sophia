package com.orange.sophia.route.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

class DataPushTest extends FunSuite with DataPush with ScalatestRouteTest with ScalaFutures {


  test("send file to the push data service") {
    val multipartForm =
      Multipart.FormData(Multipart.FormData.BodyPart.Strict(
        "csv",
        HttpEntity(ContentTypes.`text/plain(UTF-8)`, "2,3,5\n7,11,13,17,23\n29,31,37\n"),
        Map("filename" -> "primes.csv")))
    
  }
}
