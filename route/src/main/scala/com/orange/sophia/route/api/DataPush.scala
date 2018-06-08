package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, Multipart, StatusCodes}
import com.orange.sophia.route.marshall.JsonSupport
import akka.http.scaladsl.server.Directives._
import com.orange.sophia.route.actor.HdfsActor


trait DataPush extends JsonSupport {

  val hdfsActor: ActorRef = system.actorOf(Props[HdfsActor])
  implicit def system: ActorSystem

  val route = concat(
    path("push") {
      (post & entity(as[Multipart.FormData])) { fileData =>
        complete {
          HttpResponse(StatusCodes.OK)
        }
      }
    }
  )
}
