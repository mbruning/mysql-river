package org.elasticsearch.river.mysql.MysqlRiver

import org.elasticsearch.common.inject.Inject
import org.elasticsearch.river.{AbstractRiverComponent, River, RiverName, RiverSettings}
import org.elasticsearch.client.{Requests, Client}
import akka.actor.ActorSystem
import akka.actor.Props
import org.elasticsearch.handler.Master
import scala.collection.mutable.HashMap
import org.elasticsearch.common.xcontent.support.XContentMapValues

/**
 * @author ${user.name}
 */

// define actor messages
sealed trait MysqlMessage
case object Start extends MysqlMessage
case object Stop extends MysqlMessage
case class Query(params: Map[String, Any]) extends MysqlMessage
case class SQLResult(result: List[Map[String, Any]]) extends MysqlMessage
case class SQLRow(result: Map[String, Any]) extends MysqlMessage

// main river class
class MysqlRiver @Inject()(name: RiverName, settings: RiverSettings, client: Client)
  extends AbstractRiverComponent(name, settings) with River {
  val riverType: String = "mysql"
  val params = settings.settings.get(riverType).asInstanceOf[java.util.Map[String, String]]
  // get this from settings
  val query:String = params.get("query") // "select * from artist limit 10"
  val mySqlUrl: String = "jdbc:mysql://%s/%s?%s".format(params.get("hostname"), params.get("database"), params.get("dbOpts"))   // "jdbc:mysql://10.0.0.211:3306/semetric?zeroDateTimeBehavior=convertToNull"
  logger.info("===>URL {}", mySqlUrl)
  val user: String = params.get("username") // "global_read"
  val pass: String = params.get("password") // "motNigonNut2"
  val interval: String = params.get("interval") // "20000"
  val index: String = params.get("index") // "artist-test-001"
  val `type`: String = params.get("type") // "artist"
  val unique: String = params.get("uniqueIDField") // "id"
  val esHost: String = params.get("elasticsearchHost") //"http://localhost:9200"
  // setup system
  val system = ActorSystem("MySQL")
  val master = system.actorOf(Props(new Master(system, Map("index" -> index,
                                                           "type" -> `type`,
                                                           "unique" -> unique,
                                                           "query" -> query,
                                                           "url" -> mySqlUrl,
                                                           "user" -> user,
                                                           "pass" -> pass,
                                                           "eshost" -> esHost,
                                                           "interval" -> interval))), name="MasterActor")

  override def close() {
    logger.info("Closing river")
    // tell master to stop
    master ! Stop
    system.shutdown()
  }

  override def start() {
    // tell master to start
    master ! Start
    logger.info("Started master")
  }
}

