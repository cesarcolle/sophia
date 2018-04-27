package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, Props}

final case class Service(name: String, address: String, port: Integer)

/*
* https://github.com/spray/spray-json
* https://github.com/akka/akka-http-quickstart-scala.g8/blob/1f38f2edbdf5b73dbbf2ae63494cfd19dff1d517/src/main/g8/src/main/scala/%24package%24/JsonSupport.scala
* https://akka.io/docs/
* */


object ServiceActor {

  sealed trait Command

  final case class GetServices() extends Command

  final case class AddService(name: String, address: String, port: Integer) extends Command

  sealed trait ResultServiceActor

  final case class ServiceActionPerformed(message : String) extends ResultServiceActor

  final case class DiscoverServiceResult(servicesResult : List[Service]) extends ResultServiceActor

  def props: Props = Props[ServiceActor]
}

class ServiceActor extends Actor with ActorLogging {

  import ServiceActor._

  val services = Set.empty[Service]

  override def receive: Receive = {
    case GetServices =>
      log.info("receive to list services...")
      sender() ! DiscoverServiceResult(services.toList)

    case service: AddService =>
      services + Service(service.name, service.address, service.port)
      sender() ! ServiceActionPerformed
  }
}
