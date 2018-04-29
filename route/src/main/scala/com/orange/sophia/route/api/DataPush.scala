package com.orange.sophia.route.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, Multipart, StatusCodes}
import com.orange.sophia.route.marshall.JsonSupport
import akka.http.scaladsl.server.Directives._


trait DataPush extends JsonSupport {

  implicit val hdfsActor: ActorRef

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
