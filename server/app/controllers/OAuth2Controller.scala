package controllers

import java.util.Date

import models.AccessToken
import models.User
import models.Tables._
import org.joda.time.DateTime
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.Crypto
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play

import scala.concurrent.duration.Duration
import scalaoauth2.provider.{AccessToken => OAuthAccessToken}
import scalaoauth2.provider.AuthInfo
import scalaoauth2.provider.ClientCredential
import scalaoauth2.provider.DataHandler
import scalaoauth2.provider.InvalidClient
import scalaoauth2.provider.OAuth2Provider

object OAuth2Controller extends Controller with OAuth2Provider with HasDatabaseConfig[JdbcProfile] {

  val logging = Logger(this.getClass)
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  def accessToken = Action.async { implicit request =>
    issueAccessToken(new MyDataHandler())
  }

  class MyDataHandler extends DataHandler[User] {

    val TokenExpiration = Duration(60, "days")

    implicit def accessToken2OAuthAccessToken(at: AccessToken): OAuthAccessToken =
      OAuthAccessToken(at.accessToken,
        at.refreshToken,
        at.scope,
        Some(at.expiresAt.millis),
        new Date(at.createdAt.millis))

    def validateClient(clientCredential: ClientCredential, grantType: String): Future[Boolean] = {
      val q = (for {
        c <- OAuthClientTable if c.secret === clientCredential.clientSecret &&
        c.id === clientCredential.clientId
        g <- GrantTypeTable if g.grantId === grantType
      } yield grantType).exists.result
      db.run(q)
    }

    def findUser(username: String, password: String): Future[Option[User]] = {
      val q = UserTable.filter(_.username === username).result.headOption
      db.run(q)
    }

    def createAccessToken(authInfo: AuthInfo[User]): Future[OAuthAccessToken] = {
      val createdAt = new Date()
      val accessTokenExpiresIn = DateTime.now.getMillis + TokenExpiration.toMillis
      val refreshToken = Some(Crypto.generateToken)
      val accessToken = Crypto.generateToken
      val clientId = authInfo.clientId.getOrElse(throw new InvalidClient())
      val tokenObject = models.AccessToken(accessToken,
        refreshToken,
        authInfo.user.id,
        clientId,
        authInfo.scope,
        accessTokenExpiresIn,
        createdAt.getTime)
      val result = for {
        n <- db.run(AccessTokenTable += tokenObject) if n > 0
      } yield OAuthAccessToken(accessToken,
          refreshToken,
          authInfo.scope,
          Some(accessTokenExpiresIn),
          createdAt)
      result
    }

    def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[OAuthAccessToken]] = {
      val q = AccessTokenTable
        .filter(x => x.clientId === authInfo.clientId && x.userId === authInfo.user.id).result
        .headOption
      db.run(q)
    }

    def refreshAccessToken(
                            authInfo: AuthInfo[User],
                            refreshToken: String
                          ): Future[OAuthAccessToken] = ???

    def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

    def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???

    def findClientUser(
                        clientCredential: ClientCredential,
                        scope: Option[String]
                      ): Future[Option[User]] = ???

    def deleteAuthCode(code: String): Future[Unit] = ???

    def findAccessToken(token: String): Future[Option[OAuthAccessToken]] = ???

    def findAuthInfoByAccessToken(accessToken: OAuthAccessToken): Future[Option[AuthInfo[User]]] = ???

  }

}
