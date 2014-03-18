package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/12/13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

import akka.actor._
import org.elasticsearch.river.mysql.MysqlRiver.SQLResult
import org.elasticsearch.river.mysql.MysqlRiver.Start
import org.elasticsearch.river.mysql.MysqlRiver.Query
import collection.JavaConverters._
import org.elasticsearch.river.mysql.MysqlRiver._
import scala.concurrent.duration._
import akka.actor.Cancellable
import org.elasticsearch.common.logging.ESLoggerFactory

class ConfigException(msg: String) extends RuntimeException(msg)

class Master(system: ActorSystem, params: Map[String, Any]) extends Actor {
  var job:Cancellable = _
  val logger = ESLoggerFactory.getLogger(getClass.getName)
  val esActor: ActorRef = system.actorOf(Props(new ElasticsearchActor(params("index").toString,
                                                                      params("type").toString,
                                                                      params("unique").toString,
                                                                      params("eshost").toString)), name = "ESActor")
  val dbActor: ActorRef = system.actorOf(Props(new MysqlActor), name = "DBActor")

  def receive = {
    case Start => startMysqlActor()
    case SQLResult(result) => result map (r => esActor ! SQLRow(r))
    case Stop => logger.info("Master received stop!")
  }

  def startMysqlActor() {
    import system.dispatcher
    job = system.scheduler.schedule(0 milliseconds,
                                    params("interval").toString.toInt milliseconds,
                                    dbActor,
                                    {
                                      Query(Map("query" -> params("query").toString,
                                                "url" -> params("url").toString,
                                                "user" -> params("user").toString,
                                                "pass" -> params("pass").toString))
                                    })
    logger.info("Scheduled job")
  }


}

