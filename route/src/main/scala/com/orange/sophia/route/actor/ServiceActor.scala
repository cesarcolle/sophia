package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.pattern.{ask, pipe}
import scala.concurrent.duration._
import akka.util.Timeout

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
  import context.dispatcher
  import ServiceActor._

  implicit val timeout = Timeout(5 seconds) // needed for `?` below
  val persistent = context.actorOf(Props[ServiceMetricActor])

  val materializer = ActorMaterializer()


  def active(services: Map[String, Service]): Receive = {

    case service@AddService(name, address, port) =>
      context become active(services + (name -> Service(name, address, port)))
      log.info("states become :: " + services)
      val content = ask(persistent, service).mapTo[ServiceActionPerformed]
      pipe(content) to sender()
      
    case namedService@GetServiceByName(name) =>
      log.info("find service by name")
      val serviceFiltered = services.get(name)
      sender() ! NamedServices(serviceFiltered.toList)

    case GetServices =>
      sender() ! Services(services.values.toList)
  }

  override def receive: Receive = active(Map.empty)
}
