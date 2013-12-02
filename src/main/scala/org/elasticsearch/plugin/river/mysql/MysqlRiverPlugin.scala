package org.elasticsearch.plugin.river.mysql

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 02/12/13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */

import org.elasticsearch.river.mysql.MysqlRiver.MysqlRiverModule
import org.elasticsearch.plugins.AbstractPlugin
import org.elasticsearch.common.inject.{Inject, Module}
import org.elasticsearch.river.RiversModule


class MysqlRiverPlugin @Inject() extends AbstractPlugin {

  override def name = "mysql-river"

  override def description = "sync myql db with elasticsearch river"

  override def processModule(module:  Module) = {
    if (module.isInstanceOf[RiversModule]) {
      module.asInstanceOf[RiversModule].registerRiver("mysql", classOf[MysqlRiverModule])
    }
  }
}
