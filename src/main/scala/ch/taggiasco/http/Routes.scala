package ch.taggiasco.http

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import ch.taggiasco.streams.StatCall
import scala.concurrent.ExecutionContext


/**
 * Defines all the routes for the http server.
 */

trait Routes {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val executionContext: ExecutionContext
  
  
  private val statRoutes = {
    post {
      formFields(
        ('source ?, 'filter ?, 'reducer ?, 'statistic ?)
      ).as(StatCall) { statCall => {
        complete {
          statCall.execute().map(s => {
            HttpEntity(ContentTypes.`text/plain(UTF-8)`, s)
          })
        }
//        complete(
//          HttpEntity(
//            ContentTypes.`text/plain(UTF-8)`,
//            numbers.map(n => ByteString(s"$n\n"))
//          )
//        )
      }}
    }
  }
  
  val routes = {
    path("") {
      getFromResource("public/index.html")
    } ~
    path("stat") {
      statRoutes
    }
  }
  
}