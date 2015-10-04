
package models

import com.geirsson.util.Epoch

/** Entity class storing rows of table AppUserTable
 *  @param id Database column id SqlType(uuid), PrimaryKey
 *  @param username Database column username SqlType(varchar), Length(256,true), Default(None)
 *  @param profiles Database column profiles SqlType(_text), Length(2147483647,false)
 *  @param roles Database column roles SqlType(_varchar), Length(64,false)
 *  @param created Database column created SqlType(timestamp) */
case class AppUserRow(id: java.util.UUID, username: Option[String] = None, profiles: List[String], roles: String, created: Epoch)




/** Entity class storing rows of table OAuth2InfoTable
 *  @param provider Database column provider SqlType(varchar), Length(64,true)
 *  @param key Database column key SqlType(text)
 *  @param access_token Database column access_token SqlType(text)
 *  @param token_type Database column token_type SqlType(varchar), Length(64,true), Default(None)
 *  @param expires_in Database column expires_in SqlType(int4), Default(None)
 *  @param refresh_token Database column refresh_token SqlType(varchar), Length(64,true), Default(None)
 *  @param params Database column params SqlType(text), Default(None)
 *  @param created Database column created SqlType(timestamp), Default(None) */
case class OAuth2InfoRow(provider: String, key: String, access_token: String, token_type: Option[String] = None, expires_in: Option[Int] = None, refresh_token: Option[String] = None, params: Option[String] = None, created: Option[Epoch] = None)




/** Entity class storing rows of table PasswordInfoTable
 *  @param provider Database column provider SqlType(varchar), Length(64,true)
 *  @param key Database column key SqlType(text)
 *  @param hasher Database column hasher SqlType(varchar), Length(64,true)
 *  @param password Database column password SqlType(varchar), Length(256,true)
 *  @param salt Database column salt SqlType(varchar), Length(256,true), Default(None)
 *  @param created Database column created SqlType(timestamp) */
case class PasswordInfoRow(provider: String, key: String, hasher: String, password: String, salt: Option[String] = None, created: Epoch)




/** Entity class storing rows of table RequestsTable
 *  @param id Database column id SqlType(uuid), PrimaryKey
 *  @param user_id Database column user_id SqlType(uuid)
 *  @param auth_provider Database column auth_provider SqlType(varchar), Length(64,true)
 *  @param auth_key Database column auth_key SqlType(text)
 *  @param remote_address Database column remote_address SqlType(varchar), Length(64,true)
 *  @param method Database column method SqlType(varchar), Length(10,true)
 *  @param host Database column host SqlType(text)
 *  @param secure Database column secure SqlType(bool)
 *  @param path Database column path SqlType(text)
 *  @param query_string Database column query_string SqlType(text), Default(None)
 *  @param lang Database column lang SqlType(text), Default(None)
 *  @param cookie Database column cookie SqlType(text), Default(None)
 *  @param referrer Database column referrer SqlType(text), Default(None)
 *  @param user_agent Database column user_agent SqlType(text), Default(None)
 *  @param started Database column started SqlType(timestamp)
 *  @param duration Database column duration SqlType(int4)
 *  @param status Database column status SqlType(int4) */
case class RequestsRow(id: java.util.UUID, user_id: java.util.UUID, auth_provider: String, auth_key: String, remote_address: String, method: String, host: String, secure: Boolean, path: String, query_string: Option[String] = None, lang: Option[String] = None, cookie: Option[String] = None, referrer: Option[String] = None, user_agent: Option[String] = None, started: Epoch, duration: Int, status: Int)




/** Entity class storing rows of table SessionInfoTable
 *  @param id Database column id SqlType(text), PrimaryKey
 *  @param provider Database column provider SqlType(varchar), Length(64,true)
 *  @param key Database column key SqlType(text)
 *  @param last_used Database column last_used SqlType(timestamp)
 *  @param expiration Database column expiration SqlType(timestamp)
 *  @param fingerprint Database column fingerprint SqlType(text), Default(None)
 *  @param created Database column created SqlType(timestamp) */
case class SessionInfoRow(id: String, provider: String, key: String, last_used: Epoch, expiration: Epoch, fingerprint: Option[String] = None, created: Epoch)




/** Entity class storing rows of table UserProfilesTable
 *  @param provider Database column provider SqlType(varchar), Length(64,true)
 *  @param key Database column key SqlType(text)
 *  @param email Database column email SqlType(varchar), Length(256,true), Default(None)
 *  @param first_name Database column first_name SqlType(varchar), Length(512,true), Default(None)
 *  @param last_name Database column last_name SqlType(varchar), Length(512,true), Default(None)
 *  @param full_name Database column full_name SqlType(varchar), Length(512,true), Default(None)
 *  @param avatar_url Database column avatar_url SqlType(varchar), Length(512,true), Default(None)
 *  @param created Database column created SqlType(timestamp) */
case class UserProfilesRow(provider: String, key: String, email: Option[String] = None, first_name: Option[String] = None, last_name: Option[String] = None, full_name: Option[String] = None, avatar_url: Option[String] = None, created: Epoch)



         
