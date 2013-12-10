package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/12/13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

import akka.actor._
import org.elasticsearch.handler.MysqlActor
import org.elasticsearch.river.mysql.MysqlRiver.{Start, Stop, Query, SQLResult}
import org.elasticsearch.river.mysql.MysqlRiver.SQLResult
import org.elasticsearch.river.mysql.MysqlRiver.Start
import org.elasticsearch.river.mysql.MysqlRiver.Query
import scala.concurrent.duration._
import akka.actor.Cancellable
import org.elasticsearch.common.logging.ESLoggerFactory

class Master extends Actor {

  var job:Cancellable = _
  val logger = ESLoggerFactory.getLogger(getClass.getName)

  def receive = {
    case Start(system, params) => startMysqlActor(system, params)
    case Stop => job.cancel()
    case SQLResult(result) => startVoldemortActor(result)
  }

  def startVoldemortActor(result: List[Map[String, Any]]) {
    logger.info("====>STARTVOLDMORT {}", result)
  }

  def startMysqlActor(system: ActorSystem, params: Map[String, String]) {

    val query: String = params("query")
    val user: String = params("user")
    val url: String = params("url")
    val pass: String = params("pass")
    val interval: Int = params("interval").toInt
    import system.dispatcher
    val mysqlActor = system.actorOf(Props(new MysqlActor), name="MySQLActor")
    job = system.scheduler.schedule(0 milliseconds, interval milliseconds, mysqlActor, Query(Map("query" -> query,
                                                                                                 "url" -> url,
                                                                                                 "user" -> user,
                                                                                                 "pass" -> pass)))
    logger.info("Scheduled job for query:", query)
  }
}
