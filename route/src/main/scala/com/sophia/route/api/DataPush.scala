package com.orange.sophia.route.api

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.orange.sophia.route.marshall.JsonSupport
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


trait DataPush extends JsonSupport {

  implicit def system: ActorSystem

  var hdfsAdress = sys.env.getOrElse("HDFS_NAMENODE", "empty")


  def pushToHDFS(hdfsAddress : String): FileSystem ={
    val conf = new Configuration()
    conf.set("fs.defaultFS", hdfsAdress)
    FileSystem.get(conf)
  }

  val routeDataPush: Route = path("push") {
    extractRequestContext { ctx =>
      implicit val materializer: Materializer = ctx.materializer

      fileUpload("data") {
        case (metadata, byteSource) =>
          val newHDFSfile = pushToHDFS(hdfsAdress).create(new Path(metadata.fileName))
          println(metadata.fieldName + " " + metadata.fileName)
          byteSource.runForeach(byte => newHDFSfile.write(byte.utf8String.getBytes))
          complete(s"succes")

      }
    }
  }
}
