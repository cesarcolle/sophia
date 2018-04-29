package com.orange.sophia.route.actor

import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}


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

  def active(services: Map[String, Service]): Receive = {

    case service@AddService(name, address, port) =>
      context become active(services + (name -> Service(name, address, port)))

      log.info("states become :: " + services)
      sender() ! ServiceActionPerformed("services added.")

    case namedService@GetServiceByName(name) =>
      log.info("find service by name")
      val serviceFiltered = services.get(name)
      sender() ! NamedServices(serviceFiltered.toList)

    case GetServices =>
      sender() ! Services(services.values.toList)
  }

  override def receive: Receive = active(Map.empty)
}
