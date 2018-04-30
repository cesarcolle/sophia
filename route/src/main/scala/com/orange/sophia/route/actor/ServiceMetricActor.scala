package com.orange.sophia.route.actor

import akka.actor.Props
import akka.persistence.{AtLeastOnceDelivery, PersistentActor}
import com.orange.sophia.route.actor.ServiceActor._

object ServiceMetricActor {

  sealed trait PersistEvent
  case class ServicesPersist() extends PersistEvent

  sealed trait PersistCommand
  case class ServiceRecovering() extends PersistCommand

  val props = Props[ServiceMetricActor]
}

case class ServiceMetricsEntity(services : List[Service], utilization : Map[String, Int])


class ServiceMetricActor extends PersistentActor  {

  val metrics : ServiceMetricsEntity = ServiceMetricsEntity(List.empty[Service], Map.empty[String, Int])

  override def receiveRecover: Receive = {
    case service@AddService(name, address, port) =>
      metrics.services.+:(name -> Service(name, address, port))

  }

  override def receiveCommand: Receive = {
    case service@AddService(name, address, port) =>
      persist(service) { service =>
        metrics.services.+:(name -> Service(name, address, port))
        sender() ! ServiceActionPerformed("services added.")
      }
    case GetServices =>
      sender() ! Services(metrics.services)

  }

  override def persistenceId: String = "persistent-service-metrics"
}
