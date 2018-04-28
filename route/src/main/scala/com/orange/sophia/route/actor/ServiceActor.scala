package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, Props}



/*
* https://github.com/spray/spray-json
* https://github.com/akka/akka-http-quickstart-scala.g8/blob/1f38f2edbdf5b73dbbf2ae63494cfd19dff1d517/src/main/g8/src/main/scala/%24package%24/JsonSupport.scala
* https://akka.io/docs/
* */


object ServiceActor {

  sealed trait Command

  final case class GetServices() extends Command

  sealed trait ResultServiceActor


  def props: Props = Props[ServiceActor]
}

class ServiceActor extends Actor with ActorLogging {

  import ServiceActor._

  val services = Services(List())

  override def receive: Receive = {
    case GetServices =>
      log.info("receive to list services...")
      sender() ! services

    case service: AddService =>
      services.services.::(Service(service.name, service.address, service.port))
      sender() ! ServiceActionPerformed
  }
}
