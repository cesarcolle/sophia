package com.orange.sophia.route.actor

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.orange.sophia.route.actor.PersistenceServiceActor._

object PersistenceServiceActor{


  // addFormat
  case class AddFormat(name : String, schema : String)
  case class AddServiceFormat(nameService : String, formatService : List[String])

  case class StatusService(sizeStatus : Int)
  case class StatusFormat(sizeFormat : Int)

  def props: Props = Props[PersistenceServiceActor]


  // Persistent
  case class ServiceFormatState(serviceForms : Map[String, List[String]] = Map.empty, formats : Map[String, String] = Map.empty){
    def sizeFormat(): Int ={
      formats.size
    }

    def sizeService() : Int = {
      serviceForms.size
    }

    def addFormat(nameFormat : String, schema : String): ServiceFormatState ={
      copy(serviceForms, formats.updated(nameFormat, schema))
    }

    def addServiceFormat(nameService : String, formatService : List[String]): ServiceFormatState = {
      if (!formats.keys.forall(formatService.contains)) throw new IllegalArgumentException("bad format description given ")

      serviceForms.get(nameService) match {
        case Some(xs: List[String]) => copy(serviceForms.updated(nameService, xs ++ formatService ), formats)
        case None => this
      }
    }
  }
}

class PersistenceServiceActor extends PersistentActor with ActorLogging{

  var state = ServiceFormatState()

  def sizeFormat() : Int = {
    state.sizeFormat()
  }

  def sizeService() : Int = {
    state.sizeService()
  }

  def updateFormat(format : AddFormat): Unit ={
    state = state.addFormat(format.name, format.schema)
  }

  def updateServiceFormat(serviceFormat : AddServiceFormat): Unit ={
    state = state.addServiceFormat(serviceFormat.nameService, serviceFormat.formatService)
    log.info("After :: " + state.serviceForms)
  }

  override def receiveRecover: Receive = {
    case format : AddFormat => updateFormat(format)
    case SnapshotOffer(_, snapshot: ServiceFormatState) => state = snapshot
    case RecoveryCompleted => log.info("Recovery completed!")

  }

  override def receiveCommand: Receive = {

    case serviceFormat : AddServiceFormat =>
      persist(serviceFormat){event =>
        log.info("updateServiceForm")
        updateServiceFormat(event)
        sender() ! StatusService(state.sizeService())
      }
    case addFormat : AddFormat =>
      persist(addFormat) {format =>
        updateFormat(format)
        sender() ! StatusFormat(state.sizeFormat())
      }
    case _ =>
  }

  override def persistenceId: String = "service-data"
}
