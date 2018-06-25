package com.orange.sophia.route.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.ServiceActor._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._


class ServiceActorTest extends TestKit(ActorSystem("ServiceActor")) with ImplicitSender with Matchers with WordSpecLike {

  val serviceActor: ActorRef = system.actorOf(ServiceActor.props)
  val addService = AddService("hey", "ho", 1012)

  "The service actor" must {
    "register a service and send good answer" in {
      serviceActor ! addService
      expectMsg(ServiceActionPerformed("action performed"))
    }

    "find a service by name" must {
      "find nothing because it's empty" in {
        serviceActor ! GetServiceByName("test")
        expectMsg(NamedServices(List()))
      }
    }

    "add a service and find the service" must {
      "add a service " in {
        val result = serviceActor ! addService
        expectMsg(ServiceActionPerformed("action performed"))
        serviceActor ! GetServiceByName("hey")
        // wait before ...
        within(600 millis) {
          expectMsg(NamedServices(List(Service("hey", "ho", 1012), Service("hey", "ho", 1012))))
        }
      }
    }


  }

}
