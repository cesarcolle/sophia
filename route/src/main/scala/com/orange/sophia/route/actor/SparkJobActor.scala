package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorRef}
import akka.stream.ActorMaterializer
import com.orange.sophia.route.actor.SparkJobActor.DataServiceIntegrity

object SparkJobActor {

  case class DataServiceIntegrity(serviceName : String)
  case class ModelGenerator(serviceName : String)

}


class SparkJobActor extends Actor {
  val materializer = ActorMaterializer()

  val serviceActor: ActorRef =  context.actorOf(ServiceActor.props)

  override def receive: Receive = {

    case DataServiceIntegrity(serviceName) =>


    case _ =>

  }
}
