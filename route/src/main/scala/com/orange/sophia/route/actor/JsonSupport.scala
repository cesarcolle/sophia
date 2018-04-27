package com.orange.sophia.route.actor

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.orange.sophia.route.actor.ServiceActor.{DiscoverServiceResult, ServiceActionPerformed}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  val discoverMarshal = jsonFormat1(DiscoverServiceResult)
  val actionPerformedMarshal = jsonFormat1(ServiceActionPerformed)

}
