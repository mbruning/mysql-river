package org.elasticsearch.handler

import akka.actor.Actor
import org.elasticsearch.river.mysql.MysqlRiver.SQLResult
import org.elasticsearch.river.mysql.MysqlRiver.FinalData
import voldemort.client.StoreClientFactory
import voldemort.client.StoreClient
import voldemort.client.ClientConfig
import voldemort.client.SocketStoreClientFactory
import voldemort.versioning.Versioned

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/12/13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */

class VoldemortActor(params: Any) extends Actor {

  val factory = new SocketStoreClientFactory(new ClientConfig().setBootStrapUrls(params("url")))
  val client = factory.getStoreClient(params("store"))


  def receive = {
    case SQLResult(result) => sender ! FinalData(addVoldemortData(result));
  }

  def addVoldemortData(sqlData: List[Map[String, Any]]) = {
    sqlData
  }

}
