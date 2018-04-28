package com.orange.sophia.route.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.Multipart
import com.orange.sophia.route.actor.HdfsActor.HdfsWrite

object HdfsActor {

  final case class HdfsWrite(filedata : Multipart.FormData)
  final case class FileWrittingInfo(message:String)
  // TODO: See for argument to actor with address + port for hadoop.
  def props: Props = Props[ServiceActor]
}


class HdfsActor extends Actor with ActorLogging{

  override def receive: Receive = {
    case write : HdfsWrite =>
      log.info("write into hdfs")
      write.filedata.parts
  }
}
