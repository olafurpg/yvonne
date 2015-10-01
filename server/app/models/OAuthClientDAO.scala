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

trait OAuthClientError

case object ClientExistsError extends OAuthClientError

case object UnknownClientError extends OAuthClientError

trait OAuthClientDAO extends DAO {

  import driver.api._

  def insertClient(client: OAuthClient,
                   grantType: String
                  ): Future[Either[OAuthClient, OAuthClientError]] = {
    val q = (for {
      _ <- OAuthClientTable += client
      _ <- ClientGrantTypeTable += ClientGrantType(grantType, client.id)
    } yield ()).transactionally
    db.run(q)
      .map { _ =>
      Left(client)
    }.recover {
      case e: JdbcSQLException if e.getMessage.contains("Unique index") => Right(ClientExistsError)
    }
  }

  def createClient(clientId: String,
                   password: String,
                   redirectUri: Option[String],
                   scope: Option[String] = None
                  ): OAuthClient = {
    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
    OAuthClient(clientId, Some(hashedPassword), redirectUri, scope)
  }

}

object OAuthClientDAO extends OAuthClientDAO
