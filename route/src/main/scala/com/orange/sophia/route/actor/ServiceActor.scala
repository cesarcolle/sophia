package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.pattern.{ask, pipe}

import scala.concurrent.duration._
import akka.util.Timeout

import scala.collection.mutable.ListBuffer

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

  implicit val timeout = Timeout(5 seconds) // needed for `?` below
  //val persistent = context.actorOf(Props[ServiceMetricActor])

  var allServices : List[Service] = List.empty[Service]

  val materializer = ActorMaterializer()

  override def receive: Receive = {

    case service@AddService(name, address, port) =>
      allServices = Service(name, address, port) :: allServices
      sender() ! ServiceActionPerformed("action performed")

    case namedService@GetServiceByName(name) =>
      sender() ! NamedServices(List())

    case GetServices =>
      sender() ! Services(allServices.toList)
  }
}
