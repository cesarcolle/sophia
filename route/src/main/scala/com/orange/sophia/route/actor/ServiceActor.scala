package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, Props}

object ServiceActor {

  sealed trait Command

  final case class GetServices() extends Command

  sealed trait ResultServiceActor


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
      services  += Service(service.name, service.address, service.port)
      sender() ! ServiceActionPerformed("services added.")
  }
}
