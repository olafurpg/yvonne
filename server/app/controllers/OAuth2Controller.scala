package controllers

import java.util.Date

import com.geirsson.util.Epoch
import com.geirsson.util._
import models.AccessToken
import models.ClientExistsError
import models.DAO
import models.OAuth2DAO
import models.OAuthClient
import models.OAuthClientDAO
import models.User
import models.Tables._
import models.UserDAO
import models.UserDAO.UserExistsError
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

import upickle.default.write

import scalaoauth2.provider.OAuth2Provider

object OAuth2Controller
    extends Controller with OAuth2Provider with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val logging = Logger(this.getClass)

  def accessToken = Action.async { implicit request =>
    issueAccessToken(OAuth2DAO)
  }

  def registerUser(username: String, password: String) = Action.async { implicit request =>
    UserDAO.insertUser(username, password).map {
      case Left(user) => Ok(write(user))
      case Right(UserExistsError) => BadRequest(s"Username $username already exists")
      case _ => BadRequest(s"Unable to insert user $username")
    }
  }

  def registerClient(
    clientId: String,
    password: String,
    grantType: String,
    redirectUri: String,
    scope: Option[String] = None) = Action.async { implicit request =>
    val client = OAuthClientDAO.createClient(clientId, password, Some(redirectUri), scope)
    OAuthClientDAO.insertClient(client, grantType).map {
      case Left(user) => Ok(write(user))
      case Right(ClientExistsError) => BadRequest(s"Client $clientId already exists")
      case _ => BadRequest(s"Unable to insert user $clientId")
    }
  }


}
