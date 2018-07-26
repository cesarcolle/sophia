name := "route"

version := "0.1"

scalaVersion := "2.12.4"

lazy val buildSettings = Seq(
  organization := "com.orange.sophia.route",
  version := "2.4-SNAPSHOT"
)
mainClass in(Compile, run) := Some("com.sophia.route.SophiaRoute")


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1",

  "com.typesafe.akka" %% "akka-persistence" % "2.5.12",
  "org.iq80.leveldb"            % "leveldb"          % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",

  "org.apache.hadoop" % "hadoop-client" % "2.7.2" % "provided",
  "org.apache.hadoop" % "hadoop-hdfs" % "2.7.2" % "test" classifier "tests",
  "org.apache.hadoop" % "hadoop-common" % "2.7.2" % "test" classifier "tests"
)
