import akka.actor.{Props, Actor, ActorSystem}
import org.elasticsearch.handler.{Master, MysqlActor}
import org.elasticsearch.river.mysql.MysqlRiver.Start
import org.scalatest.FunSuite
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}
import akka.testkit.TestActorRef
import org.slf4j.LoggerFactory
import org.scalatest.mock.MockitoSugar
import scala.slick.jdbc.JdbcBackend.DatabaseDef

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 12/12/13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */

class MasterTest extends FunSuite with MockitoSugar {

  val logger = LoggerFactory.getLogger(getClass)

  test("sometest") {
    logger.info("***************************************")

    implicit val system = ActorSystem("test")
    val vm = Map("url" -> "someurl",
                 "store" -> "somestore")
    val params = Map("url" -> "someurl",
                    "query" -> "somequery",
                    "user" -> "someuser",
                    "pass" -> "somepass",
                    "voldemort" -> vm,
                    "interval" -> "20000")

//    class MockMysqlActor extends MysqlActor {
//      override def query(db: DatabaseDef, queryString: String) = List(Map("foo" -> "bar"))
//      override def connect(url: String, userName: String, pass: String) = "bar"
//    }
//
//    class MockMaster(system: ActorSystem, params: Map[String, Any]) extends Master(system, params) {
//      override def getDBActor = system.actorOf(Props(new MockMysqlActor), name="MysqlActor")
//    }
//
//    val masterRef = TestActorRef(new MockMaster(system, params))
//    masterRef ! Start
  }
}
