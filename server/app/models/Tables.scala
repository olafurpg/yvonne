package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
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

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = 
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema
}
