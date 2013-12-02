package org.elasticsearch.river.mysql.MysqlRiver

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 02/12/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

import org.elasticsearch.common.inject.AbstractModule
import org.elasticsearch.river.River

class MysqlRiverModule extends AbstractModule {
  override def configure = bind(classOf[River]).to(classOf[MysqlRiver]).asEagerSingleton()
}
