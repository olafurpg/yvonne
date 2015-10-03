
package models

import com.geirsson.util.Epoch

/** Entity class storing rows of table AccessTokenTable
 *  @param accessToken Database column accessToken SqlType(VARCHAR), PrimaryKey, Length(60,true)
 *  @param refreshToken Database column refreshToken SqlType(VARCHAR), Length(60,true)
 *  @param userId Database column userId SqlType(INTEGER)
 *  @param clientId Database column clientId SqlType(VARCHAR), Length(80,true)
 *  @param scope Database column scope SqlType(VARCHAR), Length(2000,true)
 *  @param expiresAt Database column expiresAt SqlType(TIMESTAMP)
 *  @param createdAt Database column createdAt SqlType(TIMESTAMP) */
case class AccessToken(accessToken: String, refreshToken: Option[String], userId: Int, clientId: String, scope: Option[String], expiresAt: Epoch, createdAt: Epoch)




/** Entity class storing rows of table AuthCodeTable
 *  @param authorizationCode Database column authorizationCode SqlType(VARCHAR), PrimaryKey, Length(40,true)
 *  @param userId Database column userId SqlType(INTEGER)
 *  @param redirectUri Database column redirectUri SqlType(VARCHAR), Length(2000,true)
 *  @param scope Database column scope SqlType(VARCHAR), Length(1000,true)
 *  @param clientId Database column clientId SqlType(VARCHAR), Length(80,true)
 *  @param createdAt Database column createdAt SqlType(TIMESTAMP)
 *  @param expiresIn Database column expiresIn SqlType(TIMESTAMP) */
case class AuthCode(authorizationCode: String, userId: Int, redirectUri: Option[String], scope: Option[String], clientId: String, createdAt: Epoch, expiresIn: Epoch)




/** Entity class storing rows of table ClientGrantTypeTable
 *  @param grantType Database column grantType SqlType(CHAR)
 *  @param clientId Database column clientId SqlType(VARCHAR), Length(80,true) */
case class ClientGrantType(grantType: String, clientId: String)




/** Entity class storing rows of table GrantTypeTable
 *  @param id Database column id SqlType(VARCHAR), PrimaryKey */
case class GrantType(id: String)




/** Entity class storing rows of table OAuthClientTable
 *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(80,true)
 *  @param secret Database column secret SqlType(VARCHAR), Length(80,true)
 *  @param redirectUri Database column redirectUri SqlType(VARCHAR), Length(2000,true)
 *  @param scope Database column scope SqlType(VARCHAR), Length(2000,true) */
case class OAuthClient(id: String, secret: Option[String], redirectUri: Option[String], scope: Option[String])




/** Entity class storing rows of table PhotoContestTable
 *  @param id Database column id SqlType(INTEGER), AutoInc, PrimaryKey
 *  @param description Database column description SqlType(VARCHAR), Length(140,true)
 *  @param startTime Database column startTime SqlType(TIMESTAMP)
 *  @param endTime Database column endTime SqlType(TIMESTAMP)
 *  @param voteStart Database column voteStart SqlType(TIMESTAMP)
 *  @param voteEnd Database column voteEnd SqlType(TIMESTAMP) */
case class PhotoContest(id: Int, description: String, startTime: Epoch, endTime: Epoch, voteStart: Epoch, voteEnd: Epoch)




/** Entity class storing rows of table UserTable
 *  @param id Database column id SqlType(INTEGER), AutoInc, PrimaryKey
 *  @param name Database column name SqlType(VARCHAR), Length(100,true)
 *  @param username Database column username SqlType(VARCHAR), Length(100,true)
 *  @param email Database column email SqlType(VARCHAR), Length(100,true)
 *  @param password Database column password SqlType(VARCHAR), Length(100,true)
 *  @param createdAt Database column createdAt SqlType(TIMESTAMP) */
case class User(id: Int, name: Option[String], username: Option[String], email: Option[String], password: Option[String], createdAt: Epoch)



         
