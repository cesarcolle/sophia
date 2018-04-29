package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, Props}

object ServiceActor {
  final case class Service(name: String, address: String, port: Int)

  final case class Services(services: List[Service])

  final case class AddService(name: String, address: String, port: Int)

  final case class ServiceActionPerformed(message: String)

  sealed trait Command
  final case class GetServices() extends Command
  final case class GetServiceByName(name: String) extends Command

  sealed trait ResultServiceActor
  final case class DescriptionService(service: Service)
  final case class NamedServices(services: List[Service])

  def props: Props = Props[ServiceActor]
}

class ServiceActor extends Actor with ActorLogging {

  import ServiceActor._

  var services: Set[Service] = Set()

  override def receive: Receive = {
    case GetServices =>
      log.info("receive to list services...")
      sender() ! Services(services.toList)

    case service: AddService =>
      log.info("add service + " + service.name)
      services += Service(service.name, service.address, service.port)
      sender() ! ServiceActionPerformed("services added.")

    case namedService : GetServiceByName =>
      log.info("searching services name : " + namedService.name)
      val filteredService= services.filter(service => service.name == namedService.name)
      log.info("find " + filteredService)
      sender() ! NamedServices(filteredService.toList)
  }
}
