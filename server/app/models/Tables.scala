package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.H2Driver
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
  lazy val schema = Array(AccessTokenTable.schema, AuthCodeTable.schema, ClientGrantTypeTable.schema, GrantTypeTable.schema, OAuthClientTable.schema, PhotoContestTable.schema, UserTable.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema


  /** GetResult implicit for fetching AccessToken objects using plain SQL queries */
  implicit def GetResultAccessToken(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[Epoch]): GR[AccessToken] = GR{
    prs => import prs._
    AccessToken.tupled((<<[String], <<?[String], <<[Int], <<[String], <<?[String], <<[Epoch], <<[Epoch]))
  }
  /** Table description of table AccessToken. Objects of this class serve as prototypes for rows in queries. */
  class AccessTokenTable(_tableTag: Tag) extends Table[AccessToken](_tableTag, "AccessToken") {
    def * = (accessToken, refreshToken, userId, clientId, scope, expiresAt, createdAt) <> (AccessToken.tupled, AccessToken.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accessToken), refreshToken, Rep.Some(userId), Rep.Some(clientId), scope, Rep.Some(expiresAt), Rep.Some(createdAt)).shaped.<>({r=>import r._; _1.map(_=> AccessToken.tupled((_1.get, _2, _3.get, _4.get, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column accessToken SqlType(VARCHAR), PrimaryKey, Length(60,true) */
    val accessToken: Rep[String] = column[String]("accessToken", O.PrimaryKey, O.Length(60,varying=true))
    /** Database column refreshToken SqlType(VARCHAR), Length(60,true) */
    val refreshToken: Rep[Option[String]] = column[Option[String]]("refreshToken", O.Length(60,varying=true))
    /** Database column userId SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("userId")
    /** Database column clientId SqlType(VARCHAR), Length(80,true) */
    val clientId: Rep[String] = column[String]("clientId", O.Length(80,varying=true))
    /** Database column scope SqlType(VARCHAR), Length(2000,true) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(2000,varying=true))
    /** Database column expiresAt SqlType(TIMESTAMP) */
    val expiresAt: Rep[Epoch] = column[Epoch]("expiresAt")
    /** Database column createdAt SqlType(TIMESTAMP) */
    val createdAt: Rep[Epoch] = column[Epoch]("createdAt")

    /** Foreign key referencing OAuthClientTable (database name FK_ACCESS_TOKEN_CLIENT_ID) */
    lazy val oAuthClientTableFk = foreignKey("FK_ACCESS_TOKEN_CLIENT_ID", clientId, OAuthClientTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserTable (database name FK_ACCESS_TOKEN_USER_ID) */
    lazy val userTableFk = foreignKey("FK_ACCESS_TOKEN_USER_ID", userId, UserTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table AccessTokenTable */
  lazy val AccessTokenTable = new TableQuery(tag => new AccessTokenTable(tag))


  /** GetResult implicit for fetching AuthCode objects using plain SQL queries */
  implicit def GetResultAuthCode(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]], e3: GR[Epoch]): GR[AuthCode] = GR{
    prs => import prs._
    AuthCode.tupled((<<[String], <<[Int], <<?[String], <<?[String], <<[String], <<[Epoch], <<[Epoch]))
  }
  /** Table description of table AuthCode. Objects of this class serve as prototypes for rows in queries. */
  class AuthCodeTable(_tableTag: Tag) extends Table[AuthCode](_tableTag, "AuthCode") {
    def * = (authorizationCode, userId, redirectUri, scope, clientId, createdAt, expiresIn) <> (AuthCode.tupled, AuthCode.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authorizationCode), Rep.Some(userId), redirectUri, scope, Rep.Some(clientId), Rep.Some(createdAt), Rep.Some(expiresIn)).shaped.<>({r=>import r._; _1.map(_=> AuthCode.tupled((_1.get, _2.get, _3, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column authorizationCode SqlType(VARCHAR), PrimaryKey, Length(40,true) */
    val authorizationCode: Rep[String] = column[String]("authorizationCode", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column userId SqlType(INTEGER) */
    val userId: Rep[Int] = column[Int]("userId")
    /** Database column redirectUri SqlType(VARCHAR), Length(2000,true) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirectUri", O.Length(2000,varying=true))
    /** Database column scope SqlType(VARCHAR), Length(1000,true) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(1000,varying=true))
    /** Database column clientId SqlType(VARCHAR), Length(80,true) */
    val clientId: Rep[String] = column[String]("clientId", O.Length(80,varying=true))
    /** Database column createdAt SqlType(TIMESTAMP) */
    val createdAt: Rep[Epoch] = column[Epoch]("createdAt")
    /** Database column expiresIn SqlType(TIMESTAMP) */
    val expiresIn: Rep[Epoch] = column[Epoch]("expiresIn")

    /** Foreign key referencing OAuthClientTable (database name FK_AUTH_CODE_CLIENT_ID) */
    lazy val oAuthClientTableFk = foreignKey("FK_AUTH_CODE_CLIENT_ID", clientId, OAuthClientTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing UserTable (database name FK_AUTH_CODE_USER_ID_USER_ID) */
    lazy val userTableFk = foreignKey("FK_AUTH_CODE_USER_ID_USER_ID", userId, UserTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table AuthCodeTable */
  lazy val AuthCodeTable = new TableQuery(tag => new AuthCodeTable(tag))


  /** GetResult implicit for fetching ClientGrantType objects using plain SQL queries */
  implicit def GetResultClientGrantType(implicit e0: GR[String]): GR[ClientGrantType] = GR{
    prs => import prs._
    ClientGrantType.tupled((<<[String], <<[String]))
  }
  /** Table description of table ClientGrantType. Objects of this class serve as prototypes for rows in queries. */
  class ClientGrantTypeTable(_tableTag: Tag) extends Table[ClientGrantType](_tableTag, "ClientGrantType") {
    def * = (grantType, clientId) <> (ClientGrantType.tupled, ClientGrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(grantType), Rep.Some(clientId)).shaped.<>({r=>import r._; _1.map(_=> ClientGrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column grantType SqlType(CHAR) */
    val grantType: Rep[String] = column[String]("grantType")
    /** Database column clientId SqlType(VARCHAR), Length(80,true) */
    val clientId: Rep[String] = column[String]("clientId", O.Length(80,varying=true))

    /** Primary key of ClientGrantTypeTable (database name PK_CLIENT_GRANT_TYPE) */
    val pk = primaryKey("PK_CLIENT_GRANT_TYPE", (grantType, clientId))

    /** Foreign key referencing GrantTypeTable (database name FK_CLIENT_GRANT_TYPE_GRANT_TYPE_ID) */
    lazy val grantTypeTableFk = foreignKey("FK_CLIENT_GRANT_TYPE_GRANT_TYPE_ID", grantType, GrantTypeTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing OAuthClientTable (database name FK_CLIENT_GRANT_TYPE_CLIENT_ID) */
    lazy val oAuthClientTableFk = foreignKey("FK_CLIENT_GRANT_TYPE_CLIENT_ID", clientId, OAuthClientTable)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table ClientGrantTypeTable */
  lazy val ClientGrantTypeTable = new TableQuery(tag => new ClientGrantTypeTable(tag))


  /** GetResult implicit for fetching GrantType objects using plain SQL queries */
  implicit def GetResultGrantType(implicit e0: GR[String]): GR[GrantType] = GR{
    prs => import prs._
    GrantType(<<[String])
  }
  /** Table description of table GrantType. Objects of this class serve as prototypes for rows in queries. */
  class GrantTypeTable(_tableTag: Tag) extends Table[GrantType](_tableTag, "GrantType") {
    def * = id <> (GrantType, GrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = Rep.Some(id).shaped.<>(r => r.map(_=> GrantType(r.get)), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table GrantTypeTable */
  lazy val GrantTypeTable = new TableQuery(tag => new GrantTypeTable(tag))


  /** GetResult implicit for fetching OAuthClient objects using plain SQL queries */
  implicit def GetResultOAuthClient(implicit e0: GR[String], e1: GR[Option[String]]): GR[OAuthClient] = GR{
    prs => import prs._
    OAuthClient.tupled((<<[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table OAuthClient. Objects of this class serve as prototypes for rows in queries. */
  class OAuthClientTable(_tableTag: Tag) extends Table[OAuthClient](_tableTag, "OAuthClient") {
    def * = (id, secret, redirectUri, scope) <> (OAuthClient.tupled, OAuthClient.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), secret, redirectUri, scope).shaped.<>({r=>import r._; _1.map(_=> OAuthClient.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(80,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(80,varying=true))
    /** Database column secret SqlType(VARCHAR), Length(80,true) */
    val secret: Rep[Option[String]] = column[Option[String]]("secret", O.Length(80,varying=true))
    /** Database column redirectUri SqlType(VARCHAR), Length(2000,true) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirectUri", O.Length(2000,varying=true))
    /** Database column scope SqlType(VARCHAR), Length(2000,true) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(2000,varying=true))
  }
  /** Collection-like TableQuery object for table OAuthClientTable */
  lazy val OAuthClientTable = new TableQuery(tag => new OAuthClientTable(tag))


  /** GetResult implicit for fetching PhotoContest objects using plain SQL queries */
  implicit def GetResultPhotoContest(implicit e0: GR[Int], e1: GR[String], e2: GR[Epoch]): GR[PhotoContest] = GR{
    prs => import prs._
    PhotoContest.tupled((<<[Int], <<[String], <<[Epoch], <<[Epoch], <<[Epoch], <<[Epoch]))
  }
  /** Table description of table PhotoContest. Objects of this class serve as prototypes for rows in queries. */
  class PhotoContestTable(_tableTag: Tag) extends Table[PhotoContest](_tableTag, "PhotoContest") {
    def * = (id, description, startTime, endTime, voteStart, voteEnd) <> (PhotoContest.tupled, PhotoContest.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(description), Rep.Some(startTime), Rep.Some(endTime), Rep.Some(voteStart), Rep.Some(voteEnd)).shaped.<>({r=>import r._; _1.map(_=> PhotoContest.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column description SqlType(VARCHAR), Length(140,true) */
    val description: Rep[String] = column[String]("description", O.Length(140,varying=true))
    /** Database column startTime SqlType(TIMESTAMP) */
    val startTime: Rep[Epoch] = column[Epoch]("startTime")
    /** Database column endTime SqlType(TIMESTAMP) */
    val endTime: Rep[Epoch] = column[Epoch]("endTime")
    /** Database column voteStart SqlType(TIMESTAMP) */
    val voteStart: Rep[Epoch] = column[Epoch]("voteStart")
    /** Database column voteEnd SqlType(TIMESTAMP) */
    val voteEnd: Rep[Epoch] = column[Epoch]("voteEnd")
  }
  /** Collection-like TableQuery object for table PhotoContestTable */
  lazy val PhotoContestTable = new TableQuery(tag => new PhotoContestTable(tag))


  /** GetResult implicit for fetching User objects using plain SQL queries */
  implicit def GetResultUser(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Epoch]): GR[User] = GR{
    prs => import prs._
    User.tupled((<<[Int], <<?[String], <<?[String], <<?[String], <<?[String], <<[Epoch]))
  }
  /** Table description of table User. Objects of this class serve as prototypes for rows in queries. */
  class UserTable(_tableTag: Tag) extends Table[User](_tableTag, "User") {
    def * = (id, name, username, email, password, createdAt) <> (User.tupled, User.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), name, username, email, password, Rep.Some(createdAt)).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2, _3, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(100,varying=true))
    /** Database column username SqlType(VARCHAR), Length(100,true) */
    val username: Rep[Option[String]] = column[Option[String]]("username", O.Length(100,varying=true))
    /** Database column email SqlType(VARCHAR), Length(100,true) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(100,varying=true))
    /** Database column password SqlType(VARCHAR), Length(100,true) */
    val password: Rep[Option[String]] = column[Option[String]]("password", O.Length(100,varying=true))
    /** Database column createdAt SqlType(TIMESTAMP) */
    val createdAt: Rep[Epoch] = column[Epoch]("createdAt")

    /** Uniqueness Index over (email) (database name IX_USER_EMAIL_UNIQUE_INDEX_2) */
    val index1 = index("IX_USER_EMAIL_UNIQUE_INDEX_2", email, unique=true)
    /** Uniqueness Index over (username) (database name IX_USER_USERNAME_UNIQUE_INDEX_2) */
    val index2 = index("IX_USER_USERNAME_UNIQUE_INDEX_2", username, unique=true)
  }
  /** Collection-like TableQuery object for table UserTable */
  lazy val UserTable = new TableQuery(tag => new UserTable(tag))
}
