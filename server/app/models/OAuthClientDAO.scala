package models
import models.Tables._

trait OAuthClientDAO extends DAO {
  import driver.api._
  def validate(clientId: String, secret: String, grantType: String): Rep[Boolean] = {
    (for {
      c <- OAuthClientTable if c.secret === secret && c.id === clientId
      g <- GrantTypeTable if g.grantId === grantType
    } yield grantType).exists
  }
}

object OAuthClientDAO extends OAuthClientDAO
