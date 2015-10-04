package models

import com.github.olafurpg.slick.Codegen
import com.github.olafurpg.slick.PostgresDriver
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

trait DAO extends JdbcBackend with Tables {
  val db = Database.forConfig("mydb")
  import profile.api._
}
