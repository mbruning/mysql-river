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
import scala.slick.jdbc.JdbcBackend.{Database, DatabaseDef}
import Q.interpolation
import org.elasticsearch.river.mysql.MysqlRiver.{Query, SQLResult}

class MysqlActor extends Actor {

  var result: List[Map[String, Any]] = _
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

  def connect(url: String, userName: String, pass: String) = {
    Database.forURL(url, user=userName, password=pass, driver="com.mysql.jdbc.Driver")
  }

  def query(db: DatabaseDef, queryString: String) = {
    db.withSession {
      implicit session =>
        sql"#$queryString".as(ResultMap).list
    }
  }

  def fetchQuery(queryParam: Map[String, Any]) = {
    try {
      val queryString: String = queryParam("query").toString
      val userName: String = queryParam("user").toString
      val url: String = queryParam("url").toString
      val pass: String = queryParam("pass").toString
      val db = connect(url, userName, pass)
      result = query(db, queryString)
    } catch {
      case exc: Exception => logger.info("MYSQL exception occurred", exc)
    }
    result
  }
}
