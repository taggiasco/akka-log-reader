package ch.taggiasco.http

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.util.ByteString

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply

import akka.stream.scaladsl._
import akka.stream.ActorMaterializer

import scala.io.StdIn
import scala.util.{Failure, Success, Random}

 
object WebServer extends Config {
  
  private val numbers = Source.fromIterator(() => Iterator.continually(Random.nextInt()))
  
  
  private val statRoutes = {
    path("test") {
      post {
        complete(
          HttpEntity(
            ContentTypes.`text/plain(UTF-8)`,
            numbers.map(n => ByteString(s"$n\n"))
          )
        )
      }
    }
  }
  
  private val routes = {
    path("") {
      getFromResource("public/index.html")
    } ~
    path("stat") {
      statRoutes
    }
  }
  
  
  def main(args: Array[String]) {
    implicit val system = ActorSystem("logreader")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    
    val log: LoggingAdapter = Logging(system, getClass)
    
    val bindingFuture = Http().bindAndHandle(routes, httpInterface, httpPort)
    bindingFuture.onComplete {
      case Failure(ex) =>
        log.error(ex, "Failed to bind to {}:{}!", httpInterface, httpPort)
      case _ =>
    }
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
  
}
