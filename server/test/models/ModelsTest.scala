package models

import org.scalatest.FlatSpec
import org.scalatestplus.play.PlaySpec
import slick.driver.H2Driver
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

trait ModelsTest extends PlaySpec with DAO {

  type DB = H2Driver.backend.DatabaseDef

  def withDb[T](testCode: DB => Future[T]): T = {
    val db = Database.forConfig("testdb")
    try {
      println(Tables.schema.createStatements)
      val create: DBIO[Unit] = DBIO.seq(
        Tables.schema.create
      )
      val f = for {
        setup <- db.run(create)
        result <- testCode(db)
      } yield result
      val obtained = Await.result(f, Duration(10, "sec"))
      obtained
    }
    finally db.close()
  }

  def equalQueries[T](directQ: DBIO[T], liftedQ: DBIO[T]): Unit = withDb { db =>
    for {
      direct <- db.run(directQ)
      lifted <- db.run(liftedQ)
    } yield (direct, lifted)
  }
}
