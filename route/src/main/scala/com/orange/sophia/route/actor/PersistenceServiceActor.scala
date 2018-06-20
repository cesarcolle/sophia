package com.orange.sophia.route.actor

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

object PersistenceServiceActor{

  case class Format(schema : String, learningField : String )

  // addFormat
  case class AddFormat(name : String, schema : String)
  case class AddServiceFormat(nameService : String, formatService : List[String])
  // answer
  case class StatusService(sizeStatus : Int)
  case class StatusFormat(sizeFormat : Int)
  // ask
  case class AskFormatForService(serviceName : String)
  case class FormatService(list: List[String])

  //

  def props: Props = Props[PersistenceServiceActor]


  // Persistent
  case class ServiceFormatState(serviceForms : Map[String, List[String]] = Map.empty, formats : Map[String, String] = Map.empty){
    def sizeFormat(): Int ={
      formats.size
    }

    def sizeService() : Int = {
      serviceForms.size
    }

    def getFormat(serviceName : String): List[String] ={
      serviceForms.get(serviceName) match {
        case Some(xs : List[String]) => xs.map(formats(_))
        case None => throw new IllegalArgumentException("bad service Name given.")
      }
    }

    def addFormat(nameFormat : String, schema : String): ServiceFormatState ={
      copy(serviceForms, formats.updated(nameFormat, schema))
    }

    def addServiceFormat(nameService : String, formatService : List[String]): ServiceFormatState = {
      if (!formats.keys.forall(formatService.contains) && serviceForms.nonEmpty) throw new IllegalArgumentException("bad format description given ")
      serviceForms.get(nameService) match {
        case Some(xs: List[String]) => copy(serviceForms.updated(nameService, xs ++ formatService ), formats)
        case None => copy(serviceForms + (nameService -> formatService), formats)
      }
    }
  }
}

class PersistenceServiceActor extends PersistentActor with ActorLogging{
  import PersistenceServiceActor._

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

    case AskFormatForService(name) =>
      sender() ! FormatService(state.getFormat(name))


    case _ =>
  }

  override def persistenceId: String = "service-data"
}
