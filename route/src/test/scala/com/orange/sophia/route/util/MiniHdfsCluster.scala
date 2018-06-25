package com.orange.sophia.route.util

import java.io.File
import java.nio.file.{Files, Paths}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
import org.apache.hadoop.hdfs.MiniDFSCluster
object MiniHdfsCluster {

  val TEST_DIR = "src/test/resources/hdfs"

  var dfsCluster: MiniDFSCluster = null
  var namenode: String = _


  def provideCluster(): String = {

    Files.createDirectory(Paths.get(TEST_DIR))
    System.clearProperty(MiniDFSCluster.PROP_TEST_BUILD_DATA)
    val cluster = new File(TEST_DIR)
    val conf = new Configuration()
    conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, cluster.getAbsolutePath)
    dfsCluster = new MiniDFSCluster.Builder(conf).numDataNodes(2).build()
    namenode = FileSystem.getDefaultUri(conf).toString
    dfsCluster.waitClusterUp()
    namenode
  }

  def writeFile(pathFile: String, hdfsPath: String): Unit = {
    val conf = new Configuration()
    conf.set("fs.defaultFS", namenode)
    var fileSystem: FileSystem = FileSystem.get(conf)

    val content = Files.readAllBytes(Paths.get(pathFile))
    val buffer = fileSystem.create(new Path(hdfsPath))
    buffer.write(content)
    buffer.close()
  }

  def listFiles(pathDirectory : String): List[String] = {
    val conf = new Configuration()
    conf.set("fs.defaultFS", namenode)
    var fileSystem: FileSystem = FileSystem.get(conf)
    var files = List.empty[String]
    val filesRemote = fileSystem.listFiles(new Path(pathDirectory), true)
    while (filesRemote.hasNext) {
      files = filesRemote.next().getPath.getName :: files
    }
    files
  }

  def shutdown(): Unit = {
    FileUtil.fullyDelete(new File(TEST_DIR))
  }


}

