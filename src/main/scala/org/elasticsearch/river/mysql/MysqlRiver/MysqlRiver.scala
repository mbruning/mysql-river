package org.elasticsearch.river.mysql.MysqlRiver

import org.elasticsearch.common.inject.Inject
import org.elasticsearch.river.{AbstractRiverComponent, River, RiverName, RiverSettings}
import org.elasticsearch.client.{Requests, Client}
import akka.actor.ActorSystem
import akka.actor.Props
import org.elasticsearch.handler.Master

/**
 * @author ${user.name}
 */

// define actor messages
sealed trait MysqlMessage
case object Start extends MysqlMessage
case object Stop extends MysqlMessage
case class Query(params: Map[String, Any]) extends MysqlMessage
case class SQLResult(result: List[Map[String, Any]]) extends MysqlMessage
case class FinalData(result: List[Map[String, Any]]) extends MysqlMessage

// main river class
class MysqlRiver @Inject()(name: RiverName, settings: RiverSettings, client: Client)
  extends AbstractRiverComponent(name, settings) with River {

  // get this from settings
  val query:String = "select * from artist limit 10"
  val mySqlUrl: String = "jdbc:mysql://10.0.0.211:3306/semetric?zeroDateTimeBehavior=convertToNull"
  val user: String = "global_read"
  val pass: String = "motNigonNut2"
  val interval: String = "20000"
  // setup system
  val system = ActorSystem("MySQL")
  val master = system.actorOf(Props(new Master(system, Map("query" -> query,
                                                           "url" -> mySqlUrl,
                                                           "user" -> user,
                                                           "pass" -> pass,
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

