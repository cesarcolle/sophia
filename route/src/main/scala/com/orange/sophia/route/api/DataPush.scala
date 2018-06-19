package com.orange.sophia.route.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.Materializer
import com.orange.sophia.route.actor.PersistenceServiceActor
import com.orange.sophia.route.actor.PersistenceServiceActor.{AskFormatForService, FormatService}
import com.orange.sophia.route.marshall.JsonSupport
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


trait DataPush extends JsonSupport {

  implicit def system: ActorSystem

  var hdfsAdress: String = sys.env.getOrElse("HDFS_NAMENODE", "empty")
  val actorFormat: ActorRef = system.actorOf(PersistenceServiceActor.props)

  def HDFSFilesystem(hdfsAddress: String): FileSystem = {
    val conf = new Configuration()
    conf.set("fs.defaultFS", hdfsAdress)
    FileSystem.get(conf)
  }

  val routeDataPush: Route = path("push") {
    parameter('nameService) { nameService =>
      val serviceFormat = ask(actorFormat, AskFormatForService(nameService)).mapTo[FormatService]
      extractRequestContext { ctx =>
        implicit val materializer: Materializer = ctx.materializer

        fileUpload("data") {
          case (metadata, byteSource) =>
            val newHDFSfile = HDFSFilesystem(hdfsAdress).create(new Path(metadata.fileName))
            byteSource.runForeach(byte => newHDFSfile.write(byte.utf8String.getBytes))
            complete(s"succes")
        }


      }
    }
  }
}
