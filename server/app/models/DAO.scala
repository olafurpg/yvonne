package models

import javax.inject.Inject

import com.github.olafurpg.slick.Codegen
import com.github.olafurpg.slick.PostgresDriver
import play.api.Mode
import play.api.Play
import play.api.db.DatabaseConfig
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

trait DAO extends HasDatabaseConfig[PostgresDriver] with Tables {
  protected val dbConfig = DatabaseConfigProvider.get[PostgresDriver](Play.current)
}

object Fixture {
  def apply(): Unit = {
    println("Running fixtures.")
  }
}

trait UserDAO extends DAO {
  import driver.api._
  def users = db.run(AppUserTable.result)
}

class UserDAOImpl @Inject() extends UserDAO {
}
