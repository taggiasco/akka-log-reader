package ch.taggiasco.http

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.util.ByteString

import akka.http.scaladsl.Http

import akka.stream.scaladsl._
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.util.{Failure, Success, Random}
import ch.taggiasco.streams.StatCall


/**
 * Definition of the http server.
 */

object WebServer extends Config with Routes {
  implicit val system = ActorSystem("logreader")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val log: LoggingAdapter = Logging(system, getClass)
  
  
  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(routes, httpConfig.interface, httpConfig.port)
    bindingFuture.onComplete {
      case Failure(ex) =>
        log.error(ex, "Failed to bind to {}:{}!", httpConfig.interface, httpConfig.port)
      case _ =>
    }
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
  
}
