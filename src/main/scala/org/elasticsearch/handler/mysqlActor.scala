package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 02/12/13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */


import akka.actor.Actor
import org.elasticsearch.common.logging.ESLogger

class mysqlActor(logger: ESLogger) extends Actor {
  def receive = {
    case query:String => logger.info("===>QUERY {}", query)
  }
}
