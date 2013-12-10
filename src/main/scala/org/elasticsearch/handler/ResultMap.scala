package org.elasticsearch.handler

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/12/13
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
// taken from here: http://stackoverflow.com/questions/20262036/slick-query-multiple-tables-databases-with-getting-column-names/20278761#20278761

import scala.slick.jdbc.{GetResult,PositionedResult}
import org.elasticsearch.common.logging.ESLoggerFactory

object ResultMap extends GetResult[Map[String, Any]] {

  val logger = ESLoggerFactory.getLogger(getClass.getName)

  def apply(pr: PositionedResult) = {
    val rs = pr.rs // <- jdbc result set
    val md = rs.getMetaData();
    val res = (1 to pr.numColumns).map{ i=> md.getColumnName(i) -> rs.getObject(i) }.toMap
    pr.nextRow // <- use Slick's advance method to avoid endless loop
    res
  }
}
