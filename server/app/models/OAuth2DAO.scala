package models

import com.geirsson.util.Epoch
import models.Tables._
import org.h2.jdbc.JdbcSQLException
import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.Crypto
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scalaoauth2.provider.DataHandler
import scala.concurrent.duration.Duration
import scalaoauth2.provider.{ AccessToken => OAuthAccessToken }
import scalaoauth2.provider.AuthInfo
import scalaoauth2.provider.ClientCredential
import scalaoauth2.provider.DataHandler
import scalaoauth2.provider.InvalidClient
import scalaoauth2.provider.OAuth2Provider

trait OAuth2DAO extends DAO with DataHandler[User] {

  import driver.api._

  val TokenExpiration = Duration(60, "days")

  implicit def accessToken2OAuthAccessToken(at: AccessToken): OAuthAccessToken =
    OAuthAccessToken(at.accessToken,
      at.refreshToken,
      at.scope,
      Some(at.createdAt.getTime - at.expiresAt.getTime),
      new java.util.Date(at.createdAt.millis))

  def validateClient(clientCredential: ClientCredential, grantType: String): Future[Boolean] = {
    val q = (for {
      c <- OAuthClientTable if c.secret === clientCredential.clientSecret &&
      c.id === clientCredential.clientId
      g <- ClientGrantTypeTable if g.grantType === grantType && g.clientId === c.id
    } yield grantType).exists.result
    db.run(q)
  }

  def findUser(username: String, password: String): Future[Option[User]] = {
    val user = UserDAO.createUser(username, password)
    val q = UserTable.filter(u => u.username === username && u.password === user.password).result
      .headOption
    db.run(q)
  }

  def createAccessToken(authInfo: AuthInfo[User]): Future[OAuthAccessToken] = {
    val createdAt = DateTime.now().getMillis
    val expiresAt = createdAt + TokenExpiration.toMillis
    val refreshToken = Some(Crypto.generateToken)
    val accessToken = Crypto.generateToken
    val clientId = authInfo.clientId.getOrElse(throw new InvalidClient())
    val tokenObject = models.AccessToken(accessToken,
      refreshToken,
      authInfo.user.id,
      clientId,
      authInfo.scope,
      expiresAt,
      createdAt)
    val result = for {
      n <- db.run(AccessTokenTable += tokenObject) if n > 0
    } yield tokenObject: OAuthAccessToken
    result
  }

  def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[OAuthAccessToken]] = {
    val q = AccessTokenTable
      .filter(x => x.clientId === authInfo.clientId && x.userId === authInfo.user.id).result
      .headOption
    for {
      at <- db.run(q)
    } yield at.map(x => x: OAuthAccessToken)
  }

  def refreshAccessToken(
                          authInfo: AuthInfo[User],
                          refreshToken: String): Future[OAuthAccessToken] = ???

  def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

  def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???

  def findClientUser(
                      clientCredential: ClientCredential,
                      scope: Option[String]): Future[Option[User]] = ???

  def deleteAuthCode(code: String): Future[Unit] = ???

  def findAccessToken(token: String): Future[Option[OAuthAccessToken]] = {
    val q = AccessTokenTable.filter(_.accessToken === token).result.headOption
    for {
      at <- db.run(q)
    } yield at.map(x => x: OAuthAccessToken)
  }

  def findAuthInfoByAccessToken(accessToken: OAuthAccessToken): Future[Option[AuthInfo[User]]] = ???

}
object OAuth2DAO extends OAuth2DAO
