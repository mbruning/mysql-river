package org.elasticsearch.river.mysql.MysqlRiver

import org.elasticsearch.common.inject.Inject
import org.elasticsearch.river.{AbstractRiverComponent, River, RiverName, RiverSettings}
import org.elasticsearch.client.{Requests, Client}
import akka.actor.ActorSystem
import akka.actor.Props
import org.elasticsearch.handler.mysqlActor
import scala.concurrent.duration._
import akka.actor.Cancellable
/**
 * @author ${user.name}
 */


class MysqlRiver @Inject()(name: RiverName, settings: RiverSettings, client: Client)
  extends AbstractRiverComponent(name, settings) with River {

  var job:Cancellable = _
  val query:String = "select * from artist limit 10"

  override def close() {
    job.cancel()
    logger.info("Closing river")
  }

  override def start() {
    val system = ActorSystem("MySQL")
    import system.dispatcher
    val mysqlActor = system.actorOf(Props(new mysqlActor(logger)), name="MySQLActor")
    job = system.scheduler.schedule(0 milliseconds, 10000 milliseconds, mysqlActor, query)
    logger.info("Scheduled job for query: {}", query)
  }

}

