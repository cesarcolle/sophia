package com.orange.sophia.route.actor

import akka.persistence.PersistentActor
import com.orange.sophia.route.actor.ServiceActor.{AddService, GetServices, Service}

object ServiceMetricActor {

  sealed trait PersistEvent
  case class ServicesPersist() extends PersistEvent

  sealed trait PersistCommand
  case class ServiceRecovering() extends PersistCommand

}

case class ServiceMetricsEntity(services : List[Service], utilization : Map[String, Int])


class ServiceMetricActor extends PersistentActor {

  val metrics : ServiceMetricsEntity = ServiceMetricsEntity(List.empty[Service], Map.empty[String, Int])

  override def receiveRecover: Receive = ???

  override def receiveCommand: Receive = {
    case AddService(name, address, port) =>
      persist(AddService) { service =>
        metrics.services.+:(Service(name, address, port))
      }
    case GetServices =>
      sender() ! metrics.services.size

  }

  override def persistenceId: String = "persistent-service-metrics"
}
