package com.orange.sophia.route.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.ServiceActor._
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.concurrent.Await


class ServiceActorTest extends TestKit(ActorSystem("serviceActor")) with ImplicitSender with Matchers with WordSpecLike {

  val serviceActor: ActorRef = system.actorOf(ServiceActor.props)
  val addService = AddService("hey", "ho", 1012)

  "The service actor" must {
    "register a service and send good answer" in {
      serviceActor ! addService
      expectMsg(ServiceActionPerformed("services added."))
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
        expectMsg(ServiceActionPerformed("services added."))
        serviceActor ! GetServiceByName("hey")
        // wait before ...
        within(600 millis){
          expectMsg(NamedServices(List(Service("hey", "ho", 1012))))
        }
      }
    }



  }

}
