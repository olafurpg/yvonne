package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = com.github.olafurpg.slick.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  
  import com.geirsson.util.Epoch
  implicit val dateTimeMapper =  MappedColumnType.base[Epoch, java.sql.Timestamp](
   { epoch =>  new java.sql.Timestamp(epoch.millis) },
   { ts    =>  Epoch(ts.getTime()) }
  )
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(AppUser.schema, OAuth2Info.schema, PasswordInfo.schema, Requests.schema, SessionInfo.schema, UserProfiles.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema


  /** GetResult implicit for fetching AppUserRow objects using plain SQL queries */
  implicit def GetResultAppUserRow(implicit e0: GR[java.util.UUID], e1: GR[Option[String]], e2: GR[List[String]], e3: GR[String], e4: GR[Epoch]): GR[AppUserRow] = GR{
    prs => import prs._
    AppUserRow.tupled((<<[java.util.UUID], <<?[String], <<[List[String]], <<[String], <<[Epoch]))
  }
  /** Table description of table app_user. Objects of this class serve as prototypes for rows in queries. */
  class AppUser(_tableTag: Tag) extends Table[AppUserRow](_tableTag, "app_user") {
    def * = (id, username, profiles, roles, created) <> (AppUserRow.tupled, AppUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), username, Rep.Some(profiles), Rep.Some(roles), Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> AppUserRow.tupled((_1.get, _2, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(uuid), PrimaryKey */
    val id: Rep[java.util.UUID] = column[java.util.UUID]("id", O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(256,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("username", O.Length(256,varying=true), O.Default(None))
    /** Database column profiles SqlType(_text), Length(2147483647,false) */
    val profiles: Rep[List[String]] = column[List[String]]("profiles", O.Length(2147483647,varying=false))
    /** Database column roles SqlType(_varchar), Length(64,false) */
    val roles: Rep[String] = column[String]("roles", O.Length(64,varying=false))
    /** Database column created SqlType(timestamp) */
    val created: Rep[Epoch] = column[Epoch]("created")

    /** Index over (profiles) (database name users_profiles_idx) */
    val index1 = index("users_profiles_idx", profiles)
    /** Index over (roles) (database name users_roles_idx) */
    val index2 = index("users_roles_idx", roles)
    /** Uniqueness Index over (username) (database name users_username_idx) */
    val index3 = index("users_username_idx", username, unique=true)
  }
  /** Collection-like TableQuery object for table AppUser */
  lazy val AppUser = new TableQuery(tag => new AppUser(tag))


  /** GetResult implicit for fetching OAuth2InfoRow objects using plain SQL queries */
  implicit def GetResultOAuth2InfoRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Option[Epoch]]): GR[OAuth2InfoRow] = GR{
    prs => import prs._
    OAuth2InfoRow.tupled((<<[String], <<[String], <<[String], <<?[String], <<?[Int], <<?[String], <<?[String], <<?[Epoch]))
  }
  /** Table description of table o_auth2_info. Objects of this class serve as prototypes for rows in queries. */
  class OAuth2Info(_tableTag: Tag) extends Table[OAuth2InfoRow](_tableTag, "o_auth2_info") {
    def * = (provider, key, access_token, token_type, expires_in, refresh_token, params, created) <> (OAuth2InfoRow.tupled, OAuth2InfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(provider), Rep.Some(key), Rep.Some(access_token), token_type, expires_in, refresh_token, params, created).shaped.<>({r=>import r._; _1.map(_=> OAuth2InfoRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column provider SqlType(varchar), Length(64,true) */
    val provider: Rep[String] = column[String]("provider", O.Length(64,varying=true))
    /** Database column key SqlType(text) */
    val key: Rep[String] = column[String]("key")
    /** Database column access_token SqlType(text) */
    val access_token: Rep[String] = column[String]("access_token")
    /** Database column token_type SqlType(varchar), Length(64,true), Default(None) */
    val token_type: Rep[Option[String]] = column[Option[String]]("token_type", O.Length(64,varying=true), O.Default(None))
    /** Database column expires_in SqlType(int4), Default(None) */
    val expires_in: Rep[Option[Int]] = column[Option[Int]]("expires_in", O.Default(None))
    /** Database column refresh_token SqlType(varchar), Length(64,true), Default(None) */
    val refresh_token: Rep[Option[String]] = column[Option[String]]("refresh_token", O.Length(64,varying=true), O.Default(None))
    /** Database column params SqlType(text), Default(None) */
    val params: Rep[Option[String]] = column[Option[String]]("params", O.Default(None))
    /** Database column created SqlType(timestamp), Default(None) */
    val created: Rep[Option[Epoch]] = column[Option[Epoch]]("created", O.Default(None))

    /** Primary key of OAuth2Info (database name pk_oauth2_info) */
    val pk = primaryKey("pk_oauth2_info", (provider, key))
  }
  /** Collection-like TableQuery object for table OAuth2Info */
  lazy val OAuth2Info = new TableQuery(tag => new OAuth2Info(tag))


  /** GetResult implicit for fetching PasswordInfoRow objects using plain SQL queries */
  implicit def GetResultPasswordInfoRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Epoch]): GR[PasswordInfoRow] = GR{
    prs => import prs._
    PasswordInfoRow.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<[Epoch]))
  }
  /** Table description of table password_info. Objects of this class serve as prototypes for rows in queries. */
  class PasswordInfo(_tableTag: Tag) extends Table[PasswordInfoRow](_tableTag, "password_info") {
    def * = (provider, key, hasher, password, salt, created) <> (PasswordInfoRow.tupled, PasswordInfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(provider), Rep.Some(key), Rep.Some(hasher), Rep.Some(password), salt, Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> PasswordInfoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column provider SqlType(varchar), Length(64,true) */
    val provider: Rep[String] = column[String]("provider", O.Length(64,varying=true))
    /** Database column key SqlType(text) */
    val key: Rep[String] = column[String]("key")
    /** Database column hasher SqlType(varchar), Length(64,true) */
    val hasher: Rep[String] = column[String]("hasher", O.Length(64,varying=true))
    /** Database column password SqlType(varchar), Length(256,true) */
    val password: Rep[String] = column[String]("password", O.Length(256,varying=true))
    /** Database column salt SqlType(varchar), Length(256,true), Default(None) */
    val salt: Rep[Option[String]] = column[Option[String]]("salt", O.Length(256,varying=true), O.Default(None))
    /** Database column created SqlType(timestamp) */
    val created: Rep[Epoch] = column[Epoch]("created")

    /** Primary key of PasswordInfo (database name pk_password_info) */
    val pk = primaryKey("pk_password_info", (provider, key))
  }
  /** Collection-like TableQuery object for table PasswordInfo */
  lazy val PasswordInfo = new TableQuery(tag => new PasswordInfo(tag))


  /** GetResult implicit for fetching RequestsRow objects using plain SQL queries */
  implicit def GetResultRequestsRow(implicit e0: GR[java.util.UUID], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]], e4: GR[Epoch], e5: GR[Int]): GR[RequestsRow] = GR{
    prs => import prs._
    RequestsRow.tupled((<<[java.util.UUID], <<[java.util.UUID], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Boolean], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<[Epoch], <<[Int], <<[Int]))
  }
  /** Table description of table requests. Objects of this class serve as prototypes for rows in queries. */
  class Requests(_tableTag: Tag) extends Table[RequestsRow](_tableTag, "requests") {
    def * = (id, user_id, auth_provider, auth_key, remote_address, method, host, secure, path, query_string, lang, cookie, referrer, user_agent, started, duration, status) <> (RequestsRow.tupled, RequestsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(user_id), Rep.Some(auth_provider), Rep.Some(auth_key), Rep.Some(remote_address), Rep.Some(method), Rep.Some(host), Rep.Some(secure), Rep.Some(path), query_string, lang, cookie, referrer, user_agent, Rep.Some(started), Rep.Some(duration), Rep.Some(status)).shaped.<>({r=>import r._; _1.map(_=> RequestsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11, _12, _13, _14, _15.get, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(uuid), PrimaryKey */
    val id: Rep[java.util.UUID] = column[java.util.UUID]("id", O.PrimaryKey)
    /** Database column user_id SqlType(uuid) */
    val user_id: Rep[java.util.UUID] = column[java.util.UUID]("user_id")
    /** Database column auth_provider SqlType(varchar), Length(64,true) */
    val auth_provider: Rep[String] = column[String]("auth_provider", O.Length(64,varying=true))
    /** Database column auth_key SqlType(text) */
    val auth_key: Rep[String] = column[String]("auth_key")
    /** Database column remote_address SqlType(varchar), Length(64,true) */
    val remote_address: Rep[String] = column[String]("remote_address", O.Length(64,varying=true))
    /** Database column method SqlType(varchar), Length(10,true) */
    val method: Rep[String] = column[String]("method", O.Length(10,varying=true))
    /** Database column host SqlType(text) */
    val host: Rep[String] = column[String]("host")
    /** Database column secure SqlType(bool) */
    val secure: Rep[Boolean] = column[Boolean]("secure")
    /** Database column path SqlType(text) */
    val path: Rep[String] = column[String]("path")
    /** Database column query_string SqlType(text), Default(None) */
    val query_string: Rep[Option[String]] = column[Option[String]]("query_string", O.Default(None))
    /** Database column lang SqlType(text), Default(None) */
    val lang: Rep[Option[String]] = column[Option[String]]("lang", O.Default(None))
    /** Database column cookie SqlType(text), Default(None) */
    val cookie: Rep[Option[String]] = column[Option[String]]("cookie", O.Default(None))
    /** Database column referrer SqlType(text), Default(None) */
    val referrer: Rep[Option[String]] = column[Option[String]]("referrer", O.Default(None))
    /** Database column user_agent SqlType(text), Default(None) */
    val user_agent: Rep[Option[String]] = column[Option[String]]("user_agent", O.Default(None))
    /** Database column started SqlType(timestamp) */
    val started: Rep[Epoch] = column[Epoch]("started")
    /** Database column duration SqlType(int4) */
    val duration: Rep[Int] = column[Int]("duration")
    /** Database column status SqlType(int4) */
    val status: Rep[Int] = column[Int]("status")

    /** Foreign key referencing AppUser (database name requests_users_fk) */
    lazy val appUserFk = foreignKey("requests_users_fk", user_id, AppUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Requests */
  lazy val Requests = new TableQuery(tag => new Requests(tag))


  /** GetResult implicit for fetching SessionInfoRow objects using plain SQL queries */
  implicit def GetResultSessionInfoRow(implicit e0: GR[String], e1: GR[Epoch], e2: GR[Option[String]]): GR[SessionInfoRow] = GR{
    prs => import prs._
    SessionInfoRow.tupled((<<[String], <<[String], <<[String], <<[Epoch], <<[Epoch], <<?[String], <<[Epoch]))
  }
  /** Table description of table session_info. Objects of this class serve as prototypes for rows in queries. */
  class SessionInfo(_tableTag: Tag) extends Table[SessionInfoRow](_tableTag, "session_info") {
    def * = (id, provider, key, last_used, expiration, fingerprint, created) <> (SessionInfoRow.tupled, SessionInfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(provider), Rep.Some(key), Rep.Some(last_used), Rep.Some(expiration), fingerprint, Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> SessionInfoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(text), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column provider SqlType(varchar), Length(64,true) */
    val provider: Rep[String] = column[String]("provider", O.Length(64,varying=true))
    /** Database column key SqlType(text) */
    val key: Rep[String] = column[String]("key")
    /** Database column last_used SqlType(timestamp) */
    val last_used: Rep[Epoch] = column[Epoch]("last_used")
    /** Database column expiration SqlType(timestamp) */
    val expiration: Rep[Epoch] = column[Epoch]("expiration")
    /** Database column fingerprint SqlType(text), Default(None) */
    val fingerprint: Rep[Option[String]] = column[Option[String]]("fingerprint", O.Default(None))
    /** Database column created SqlType(timestamp) */
    val created: Rep[Epoch] = column[Epoch]("created")

    /** Index over (provider,key) (database name idx_session_info_provider_key) */
    val index1 = index("idx_session_info_provider_key", (provider, key))
  }
  /** Collection-like TableQuery object for table SessionInfo */
  lazy val SessionInfo = new TableQuery(tag => new SessionInfo(tag))


  /** GetResult implicit for fetching UserProfilesRow objects using plain SQL queries */
  implicit def GetResultUserProfilesRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Epoch]): GR[UserProfilesRow] = GR{
    prs => import prs._
    UserProfilesRow.tupled((<<[String], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<[Epoch]))
  }
  /** Table description of table user_profiles. Objects of this class serve as prototypes for rows in queries. */
  class UserProfiles(_tableTag: Tag) extends Table[UserProfilesRow](_tableTag, "user_profiles") {
    def * = (provider, key, email, first_name, last_name, full_name, avatar_url, created) <> (UserProfilesRow.tupled, UserProfilesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(provider), Rep.Some(key), email, first_name, last_name, full_name, avatar_url, Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> UserProfilesRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column provider SqlType(varchar), Length(64,true) */
    val provider: Rep[String] = column[String]("provider", O.Length(64,varying=true))
    /** Database column key SqlType(text) */
    val key: Rep[String] = column[String]("key")
    /** Database column email SqlType(varchar), Length(256,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(256,varying=true), O.Default(None))
    /** Database column first_name SqlType(varchar), Length(512,true), Default(None) */
    val first_name: Rep[Option[String]] = column[Option[String]]("first_name", O.Length(512,varying=true), O.Default(None))
    /** Database column last_name SqlType(varchar), Length(512,true), Default(None) */
    val last_name: Rep[Option[String]] = column[Option[String]]("last_name", O.Length(512,varying=true), O.Default(None))
    /** Database column full_name SqlType(varchar), Length(512,true), Default(None) */
    val full_name: Rep[Option[String]] = column[Option[String]]("full_name", O.Length(512,varying=true), O.Default(None))
    /** Database column avatar_url SqlType(varchar), Length(512,true), Default(None) */
    val avatar_url: Rep[Option[String]] = column[Option[String]]("avatar_url", O.Length(512,varying=true), O.Default(None))
    /** Database column created SqlType(timestamp) */
    val created: Rep[Epoch] = column[Epoch]("created")

    /** Index over (email) (database name user_profiles_email_idx) */
    val index1 = index("user_profiles_email_idx", email)
    /** Uniqueness Index over (provider,key) (database name user_profiles_provider_key_idx) */
    val index2 = index("user_profiles_provider_key_idx", (provider, key), unique=true)
  }
  /** Collection-like TableQuery object for table UserProfiles */
  lazy val UserProfiles = new TableQuery(tag => new UserProfiles(tag))
}
