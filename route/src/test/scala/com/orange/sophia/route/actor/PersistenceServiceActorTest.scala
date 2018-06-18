package com.orange.sophia.route.actor

import java.io.{File, FileInputStream}
import java.util.Properties

import akka.actor.{ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.testkit.{ImplicitSender, TestKit}
import com.orange.sophia.route.actor.PersistenceServiceActor.{AddFormat, AddServiceFormat, StatusFormat, StatusService}
import com.orange.sophia.route.actor.RestartableActor.{RestartActor, RestartActorException}
import org.apache.hadoop.fs.FileUtil
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

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

class PersistenceServiceActorTest extends TestKit(ActorSystem("ServiceMetrics")) with Matchers with WordSpecLike with ImplicitSender with BeforeAndAfterAll {

  var format = AddFormat("format1", "{a : integer, b : integer}")
  var journalLocation: String = _


  override def beforeAll(): Unit = {
    val properties = new Properties()
    properties.load(new FileInputStream("src/main/resources/application.conf"))
    journalLocation = properties.getProperty("akka.persistence.journal.leveldb.dir")
    FileUtil.fullyDelete(new File("persistence"))

  }

  override def afterAll(): Unit = {
    FileUtil.fullyDelete(new File("persistence"))
  }

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
      receiveN(1)

      serviceActor ! AddServiceFormat("service1", List("format1"))
      expectMsg(StatusService(1))
    }
  }

}
