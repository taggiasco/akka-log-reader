package ch.taggiasco.http

import com.typesafe.config.ConfigFactory


/**
 * Configuration of the http server and the log reader.
 */
trait Config {
  private val configuration = ConfigFactory.load()
  private val httpConfiguration = configuration.getConfig("http")
  private val logReaderConfiguration = configuration.getConfig("logreader")
  
  val httpConfig = HttpConfig(
    httpConfiguration.getString("interface"),
    httpConfiguration.getInt("port")
  )
  
  val logReaderConfig = LogReaderConfig(
    logReaderConfiguration.getString("folder")
  )
  
}
