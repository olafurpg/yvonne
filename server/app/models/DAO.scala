package models

import javax.inject.Inject
import javax.inject.Singleton
import com.github.olafurpg.slick.PostgresDriver
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserDAO @Inject() (val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[PostgresDriver] {
  import driver.api._
  def users = Future.successful(Seq[String]("str"))
//    val q = (for {
//      _ <- AppUserTable += AppUserRow(0, Some("olafurpg"), List("admin", "user"))
//      result <- AppUserTable.result
//    } yield result).transactionally
//    db.run(q)
//  }
//  def insert(user: AppUserRow) = {
//    db.run(AppUserTable += user)
//  }
}
