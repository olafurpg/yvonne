package controllers

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.Play

import scalaoauth2.provider.AccessToken
import scalaoauth2.provider.AuthInfo
import scalaoauth2.provider.ClientCredential
import scalaoauth2.provider.DataHandler
import scalaoauth2.provider.OAuth2Provider

object Oauth extends Controller with OAuth2Provider with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  def accessToken = Action.async { implicit request =>
    issueAccessToken(new MyDataHandler())
  }

  class MyDataHandler extends DataHandler[User] {

    def validateClient(clientCredential: ClientCredential, grantType: String): Future[Boolean] = ???

    def findUser(username: String, password: String): Future[Option[User]] = ???

    def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = ???

    def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = ???

    def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = ???

    def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

    def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???

    def findClientUser(clientCredential: ClientCredential, scope: Option[String]): Future[Option[User]] = ???

    def deleteAuthCode(code: String): Future[Unit] = ???

    def findAccessToken(token: String): Future[Option[AccessToken]] = ???

    def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] = ???

  }

}
