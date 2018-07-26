package com.orange.sophia.route.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.orange.sophia.route.util.MiniHdfsCluster
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class DataPushTest extends FunSuite with DataPush with ScalatestRouteTest with BeforeAndAfterAll{

  override def beforeAll(): Unit = {
    hdfsAdress = MiniHdfsCluster.provideCluster()
  }

  override def afterAll(): Unit = {
    MiniHdfsCluster.shutdown()
  }

  test("testRouteDataPush") {

    // curl --form "csv=@uploadFile.txt" http://<host>:<port>
    val multiPart = Multipart.FormData(Multipart.FormData.BodyPart.Strict(
      "csv",
      HttpEntity(ContentTypes.`text/plain(UTF-8)`, "2,3,5\n7,11,13,17,23\n29,31,37\n"),
      Map("filename" -> "primes.csv")))

    // Send data ...
    Post("/push", multiPart) ~> routeDataPush ~> check {
      responseAs[String].eq("success")
    }

    val files = MiniHdfsCluster.listFiles("/")
    println(files)
  }

}
