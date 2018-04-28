package com.orange.sophia.route.actor
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class Service(name: String, address: String, port: Int)
final case class Services(services : List[Service])
final case class AddService(name: String, address: String, port: Int)
final case class ServiceActionPerformed(message : String)


// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val serviceFormat = jsonFormat3(Service)
  implicit val servicesFormat = jsonFormat1(Services)

  implicit val addServiceFormat = jsonFormat3(AddService)
  implicit val serviceActionPerformedFormat = jsonFormat1(ServiceActionPerformed)
}