package com.orange.sophia.route.actor

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.persistence.PersistentActor
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.PersistenceServiceActor.{AddFormat, AddServiceFormat, StatusFormat, StatusService}
import com.orange.sophia.route.actor.RestartableActor.{RestartActor, RestartActorException}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, WordSpecLike}

import scala.concurrent.duration._

// To force restarting the actor.
// You restart actor => no more data but you can retry persistent data !

trait RestartableActor extends PersistentActor {

  abstract override def receiveCommand = super.receiveCommand orElse {
    case RestartActor => throw RestartActorException
  }
}

object RestartableActor {
  case object RestartActor

  private object RestartActorException extends Exception
}
class PersistenceServiceActorTest extends  TestKit(ActorSystem("ServiceMetrics"))   with Matchers with WordSpecLike with ImplicitSender {

  var format = AddFormat("format1", "{a : integer, b : integer}")

  "persistence actor" should {
    "accept a new format" in {

      val serviceActor = system.actorOf(Props(new PersistenceServiceActor() with RestartableActor))
      serviceActor ! format
      expectMsg(StatusFormat(1))
      // kill the actoooor
      serviceActor ! RestartActor

      serviceActor ! AddFormat("format2", "{b: string}")
      expectMsg(StatusFormat(2))
    }

    "add new format to a service" in {
      val serviceActor = system.actorOf(Props(new PersistenceServiceActor() with RestartableActor))
      // create a format.
      serviceActor ! format
      expectMsg(StatusFormat(1))

      serviceActor ! AddServiceFormat("service1", List("format1"))
      expectMsg(StatusService(1))
    }
  }

}
