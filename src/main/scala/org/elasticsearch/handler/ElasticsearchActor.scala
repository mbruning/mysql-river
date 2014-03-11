package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 11/03/14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */

import akka.actor.Actor
import org.elasticsearch.river.mysql.MysqlRiver.SQLResult
import org.elasticsearch.common.logging.ESLoggerFactory


class ElasticsearchActor extends Actor {
  val logger = ESLoggerFactory.getLogger(getClass.getName)

  def receive = {
    case SQLResult(data) => injectData(data)
  }
  def injectData(data: List[Map[String, Any]]) {
    logger.info("===>ADD DATA TO ES {}", data)
  }
}

