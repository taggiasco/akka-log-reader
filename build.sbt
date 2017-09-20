name := """akka-log-reader"""

organization := "ch.taggiasco"

version := "0.0.1"

scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion     = "2.5.4"
  val akkaHttpVersion = "10.0.10"

  Seq(
    "com.typesafe.akka" %% "akka-http-core"      % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"         % akkaVersion,
    "org.scalatest"     %% "scalatest"           % "3.0.1"     % "test",
    "com.typesafe.akka" %% "akka-testkit"        % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test"
  )
}
