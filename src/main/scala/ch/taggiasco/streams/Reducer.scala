package ch.taggiasco.streams

import scala.util.matching.Regex


object Reducer {
  
  private lazy val reducers: Map[String, LogEntry => Particularity] =
    Map(
      "identity" -> identity,
      "http"     -> httpMethod,
      "status"   -> status,
      "path"     -> path,
      "pathonly" -> pathOnly,
      "datehour" -> dateHour
    )
  
  
  def list() = reducers.keys.toList
  
  def find(name: String): Option[LogEntry => Particularity] = {
    reducers.get(name)
  }
  
  
  
  val identity: LogEntry => Particularity = logEntry => {
    IdentityParticularity()
  }
  
  
  val httpMethod: LogEntry => Particularity = logEntry => SingleParticularity(logEntry.httpMethod)
  
  
  val status: LogEntry => Particularity = logEntry => SingleParticularity(logEntry.status.toString())
  
  
  private val SimpleURL = "(.+)\\?(.*)".r
  private val FullURL   = "(.+)".r
  
  val pathOnly: LogEntry => Particularity = logEntry => {
    SingleParticularity(
      logEntry.url match {
        case SimpleURL(path, queryString) => path
        case FullURL(path) => path
      }
    )
  }
  
  
  val path: LogEntry => Particularity = logEntry => {
    SingleParticularity(logEntry.url)
  }
  
  
  val dateHour: LogEntry => Particularity = logEntry => {
    val datas = logEntry.date.split(" ").toList
    DualParticularity(datas(0), datas(1).split(":").head)
  }
  
  
}