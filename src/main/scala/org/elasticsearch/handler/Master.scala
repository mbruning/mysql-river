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

//import org.elasticsearch.handler.MysqlActor
import org.elasticsearch.river.mysql.MysqlRiver._
import scala.concurrent.duration._
import akka.actor.Cancellable
import org.elasticsearch.common.logging.ESLoggerFactory

class Master(system: ActorSystem, params: Map[String, Any]) extends Actor {

  var job:Cancellable = _
  val logger = ESLoggerFactory.getLogger(getClass.getName)

  def receive = {
    case Start => startMysqlActor()
    case Stop => job.cancel()
    case SQLResult(result) => startVoldemortActor(result)
    case FinalData(result) => addToElasticSearch(result)
  }

  def startVoldemortActor(result: List[Map[String, Any]]) {
    val voldemortActor = system.actorOf(Props(new VoldemortActor(params("voldemort"))), name="VoldemortActor")
    voldemortActor ! SQLResult(result)
  }

  def addToElasticSearch(data: List[Map[String, Any]]) {
    logger.info("====>ADDTOES {}", data)
  }

  def startMysqlActor() {

    val query: String = params("query").toString
    val user: String = params("user").toString
    val url: String = params("url").toString
    val pass: String = params("pass").toString
    val interval: Int = params("interval").toString.toInt
    import system.dispatcher
    val mysqlActor = system.actorOf(Props(new MysqlActor), name="MySQLActor")
    job = system.scheduler.schedule(0 milliseconds, interval milliseconds, mysqlActor, Query(Map("query" -> query,
                                                                                                 "url" -> url,
                                                                                                 "user" -> user,
                                                                                                 "pass" -> pass)))
    logger.info("Scheduled job for query:", query)
  }
}

