package models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile

trait DAO extends HasDatabaseConfig[JdbcProfile] with Tables {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val profile = dbConfig.driver
  import driver.api._
  val users = UserTable.returning(UserTable.map(_.id)) into ((user, id) => user.copy(id = id))
}