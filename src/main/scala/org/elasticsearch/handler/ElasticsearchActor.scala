package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 11/03/14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */

import akka.actor.Actor
import org.elasticsearch.river.mysql.MysqlRiver.SQLRow
import org.elasticsearch.common.logging.ESLoggerFactory
import scala.concurrent.Await
import scala.concurrent.duration._
import wabisabi._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._


class ElasticsearchActor(esIndex: String, esType: String,unique: String = "id", esHost: String = "http://localhost:9200") extends Actor {
  val logger = ESLoggerFactory.getLogger(getClass.getName)
  val client = new Client(esHost)
  implicit val formats = net.liftweb.json.DefaultFormats

  def receive = {
    case SQLRow(data) => indexData(data)
  }

  def indexData(rowData: Map[String, Any]) {
    val rowId = rowData(unique).toString
    val rowDataJSON = compact(render(decompose(rowData.filter(_._1 != unique))))
    logger.info(f"Adding data to index $esIndex%s/$esType%s with id $rowId%s")
    try {
      client.index(index = esIndex, `type` = esType, id = Some(rowId), data = rowDataJSON, refresh = true)
    } catch {
      case exc: Exception => logger.info("ES exception occurred", exc)
    }
  }
}
