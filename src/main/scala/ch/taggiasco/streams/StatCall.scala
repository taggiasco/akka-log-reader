package ch.taggiasco.streams

import ch.taggiasco.util.With
import scala.concurrent.{Future, ExecutionContext}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import akka.NotUsed


/**
 * Definition of a statistic call
 * 
 * @param source define all the sources to read
 * @param filter define the filter to apply to the log lines
 * @param reducer define the reducer to apply
 * @param statistic define the operation to apply
 */

case class StatCall(
  source:       String,
  pathFilter:   Option[String],
  methodFilter: Option[String],
  reducer:      String,
  statistic:    String
) {
  
  case class ParamException(msg: String) extends Exception
  
  private def filterFlow(f: LogEntry => Boolean): Flow[LogEntry, LogEntry, NotUsed] = Flow[LogEntry].filter(f)
  
  
  private def applyFilter(value: Option[String], f: String => (LogEntry => Boolean)): Flow[LogEntry, LogEntry, NotUsed] = {
    value match {
      case Some(v) => Flow[LogEntry].filter(f(v))
      case None => Flow[LogEntry]
    }
  }
  
  
  private def reduceFlow(f: LogEntry => Particularity): Flow[LogEntry, (Particularity, LogEntry), NotUsed] = {
    Flow[LogEntry].map(logEntry => (f(logEntry), logEntry))
  }
  
  
  def execute()(implicit system: ActorSystem, materializer: ActorMaterializer, executionContext: ExecutionContext): Future[List[String]] = {
    try {
      val streamSource    = LogFile.fromFile("src/main/resources/" + source)
      val reducerOperator = Reducer.find(reducer).getOrElse( throw ParamException("No reducer found") )
      val statisticSink   = Statistic.find(statistic).getOrElse( throw ParamException("No statistic found") )
      
      val pathFilterFlow = applyFilter(pathFilter, v => Filter.pathPattern(v.r))
      val methFilterFlow = applyFilter(methodFilter, v => Filter.httpMethod(v))
      
      val graph =
        streamSource
          .via(LogEntry.flow)
          .via(pathFilterFlow)
          .via(methFilterFlow)
          .via(reduceFlow(reducerOperator))
          .runWith(statisticSink(executionContext))
      
      graph.map(results => {
        results.map(res => s"${res._1.label} : ${res._2}").toList
      })
    } catch {
      case ParamException(msg) => Future.successful(List(msg))
    }
  }
  
}