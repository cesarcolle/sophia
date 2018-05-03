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

  var allServices : ListBuffer[Service] =  ListBuffer.empty

  val materializer = ActorMaterializer()


  def active(services: Map[String, Service]): Receive = {

    case service@AddService(name, address, port) =>
      log.info("before add :: " + allServices)
      allServices.append(Service(name, address, port))
      log.info("after add :: " + allServices)
      sender() ! ServiceActionPerformed("GG")
      
    case namedService@GetServiceByName(name) =>
      log.info("find service by name")
      log.info("services ::" + allServices)
      //val serviceFiltered = services.get(name)
      sender() ! NamedServices(List())

    case GetServices =>
      log.info("services ::" + allServices)
      sender() ! Services(allServices.toList)
  }

  override def receive: Receive = active(Map.empty)
}
