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
    case Stop => logger.info("Master received stop!"); job.cancel()
  }

  def startMysqlActor() {
    import system.dispatcher
    val interval: Any = params("interval")
    val query: Query = {
      Query(Map("query" -> params("query").toString,
        "url" -> params("url").toString,
        "user" -> params("user").toString,
        "pass" -> params("pass").toString))
    }
    if (interval == null) {
      system.scheduler.scheduleOnce(0 milliseconds, dbActor, query)
      logger.info("Scheduled single job")
    } else {
      job = system.scheduler.schedule(0 milliseconds,
                                      interval.toString.toInt milliseconds,
                                      dbActor,
                                      query)
      logger.info("Scheduled job with interval {}", interval.toString)
    }
  }


}

