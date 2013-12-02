package org.elasticsearch.river.mysql.MysqlRiver

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.river.{AbstractRiverComponent, River, RiverName, RiverSettings}
import org.elasticsearch.client.{Requests, Client}

/**
 * @author ${user.name}
 */
//object MysqlRiver {
//
//  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
//
//  def main(args : Array[String]) {
//    println( "Hello World!" )
//    println("concat arguments = " + foo(args))
//  }
//
//}

class MysqlRiver @Inject()(name: RiverName, settings: RiverSettings, client: Client)
  extends AbstractRiverComponent(name, settings) with River {

  logger.info("***************************************************")

  override def close() {
    logger.info("+++++++++++++++++++++++++++++++++")
  }

  override def start() {
    logger.info("==================================")
  }

}

