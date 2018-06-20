package com.orange.sophia.route.actor

import akka.actor.Actor
import com.orange.sophia.route.actor.SparkJobActor.DataServiceIntegrity

object SparkJobActor {

  case class DataServiceIntegrity(serviceName : String)
  case class ModelGenerator(serviceName : String)

}


class SparkJobActor extends Actor {
  val serviceActor

  override def receive: Receive = {

    case DataServiceIntegrity(serviceName) =>


    case _ =>

  }
}
