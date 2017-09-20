package ch.taggiasco.streams

import scala.concurrent.{Future, ExecutionContext}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer


/**
 * Definition of a statistic call
 * 
 * @param source define all the sources to read
 * @param filter define the filter to apply to the log lines
 * @param reducer define the reducer to apply
 * @param statistic define the operation to apply
 */

case class StatCall(
  source:    Option[String],
  filter:    Option[String],
  reducer:   Option[String],
  statistic: Option[String]
) {
  
  
  def execute()(implicit system: ActorSystem, materializer: ActorMaterializer, executionContext: ExecutionContext): Future[String] = {
    ???
  }
  
}