package com.orange.sophia.route.actor

import akka.persistence.PersistentActor
import com.orange.sophia.route.actor.PersistenceServiceActor.{AddFormat, ServiceFormatState}

object PersistenceServiceActor{


  // addFormat
  case class AddFormat(name : String, schema : String)

  // Persistent
  case class ServiceFormatState(serviceForms : Map[String, List[String]] = Map.empty, formats : Map[String, String] = Map.empty){
    def sizeFormat(): Int ={
      serviceForms.size
    }

    def addFormat(nameFormat : String, schema : String): ServiceFormatState ={
      copy(serviceForms, formats.updated(nameFormat, schema))
    }

    def addServiceFormat(formatService : List[String], nameService : String): ServiceFormatState = {
      if (!formats.keys.forall(formatService.contains)) throw new IllegalArgumentException("bad format description given ")

      serviceForms.get(nameService) match {
        case Some(xs: List[String]) => copy(serviceForms.updated(nameService, xs :+ "" ), formats)
        case None => this
      }
    }
  }
}

class PersistenceServiceActor extends PersistentActor{

  var state = ServiceFormatState()

  def updateFormat(format : AddFormat): Unit ={
    state = state.addFormat(format.name, format.schema)
  }


  override def receiveRecover: Receive = {
    case _ =>
  }

  override def receiveCommand: Receive = {

    case _ =>

  }

  override def persistenceId: String = "service-data"
}
