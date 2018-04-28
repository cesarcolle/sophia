package com.orange.sophia.route.marshall

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.orange.sophia.route.actor.ServiceActor._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}




trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val serviceFormat: RootJsonFormat[Service] = jsonFormat3(Service)
  implicit val servicesFormat: RootJsonFormat[Services] = jsonFormat1(Services)

  implicit val addServiceFormat: RootJsonFormat[AddService] = jsonFormat3(AddService)
  implicit val serviceActionPerformedFormat: RootJsonFormat[ServiceActionPerformed] = jsonFormat1(ServiceActionPerformed)
}