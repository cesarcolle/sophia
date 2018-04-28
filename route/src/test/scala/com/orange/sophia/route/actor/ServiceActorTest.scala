package com.orange.sophia.route.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.ServiceActor.{AddService, ServiceActionPerformed}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, WordSpecLike}



class ServiceActorTest extends TestKit(ActorSystem("serviceActor")) with ImplicitSender with Matchers with WordSpecLike{

  val serviceActor : ActorRef = system.actorOf(ServiceActor.props)

  "The service actor" must {

    "register a service" in {
      val addService = AddService("hey", "ho", 1012)
      serviceActor ! addService
      expectMsg(ServiceActionPerformed("services added."))
    }

  }

}
