package models

import com.geirsson.util.Epoch
import models.Tables._
import org.h2.jdbc.JdbcSQLException
import org.mindrot.jbcrypt.BCrypt
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait UserDAO extends DAO {

  import driver.api._

  def insertUser(username: String, password: String): Future[Either[User, UserError]] = {
    val newUser = UserDAO.createUser(username, password)
    db.run(users += newUser)
      .map(Left.apply).recover {
        case e: JdbcSQLException if e.getMessage.contains("Unique index") => Right(UserExistsError)
      }
  }

  def createUser(username: String, password: String): User = {
    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
    User(0, None, Some(username), None, Some(hashedPassword), Epoch.now)
  }

  trait UserError

  case object UserExistsError extends UserError

  case object UnknownUserError extends UserError

}

object UserDAO extends UserDAO
