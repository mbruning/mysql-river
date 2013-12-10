package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 02/12/13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */

import collection.JavaConversions._
import akka.actor.Actor
import org.elasticsearch.common.logging.ESLoggerFactory
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import scala.slick.jdbc.JdbcBackend.Database
import Q.interpolation
//import org.elasticsearch.handler.ResultMap
import org.elasticsearch.river.mysql.MysqlRiver.{Query, SQLResult}

class MysqlActor extends Actor {

  val logger = ESLoggerFactory.getLogger(getClass.getName)
  // load driver first
  try {
    Class.forName("com.mysql.jdbc.Driver").newInstance
  } catch {
    case e: Exception => logger.info("Exception loading JDBC driver: {}", e)
  }

  def receive = {
    case Query(params) => sender ! SQLResult(fetchQuery(params))
  }

  def fetchQuery(queryParam: Map[String, String]) = {
    val queryString: String = queryParam("query")
    val userName: String = queryParam("user")
    val url: String = queryParam("url")
    val pass: String = queryParam("pass")
    val db = Database.forURL(url, user=userName, password=pass, driver="com.mysql.jdbc.Driver")
    val result = db.withSession {
      implicit session =>
        sql"#$queryString".as(ResultMap).list
    }
    result
  }
}
