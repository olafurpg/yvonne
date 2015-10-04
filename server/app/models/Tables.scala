package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile = com.github.olafurpg.slick.PostgresDriver
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = AppUserTable.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table AppUserTable
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(256,true), Default(None)
   *  @param profiles Database column profiles SqlType(_text), Length(2147483647,false) */
  case class AppUserRow(id: Int, username: Option[String] = None, profiles: List[String])
  /** GetResult implicit for fetching AppUserRow objects using plain SQL queries */
  implicit def GetResultAppUserRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[List[String]]): GR[AppUserRow] = GR{
    prs => import prs._
    AppUserRow.tupled((<<[Int], <<?[String], <<[List[String]]))
  }
  /** Table description of table app_user. Objects of this class serve as prototypes for rows in queries. */
  class AppUserTable(_tableTag: Tag) extends Table[AppUserRow](_tableTag, "app_user") {
    def * = (id, username, profiles) <> (AppUserRow.tupled, AppUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), username, Rep.Some(profiles)).shaped.<>({r=>import r._; _1.map(_=> AppUserRow.tupled((_1.get, _2, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(256,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("username", O.Length(256,varying=true), O.Default(None))
    /** Database column profiles SqlType(_text), Length(2147483647,false) */
    val profiles: Rep[List[String]] = column[List[String]]("profiles", O.Length(2147483647,varying=false))
  }
  /** Collection-like TableQuery object for table AppUserTable */
  lazy val AppUserTable = new TableQuery(tag => new AppUserTable(tag))
}
