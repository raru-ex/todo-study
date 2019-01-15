package net.syrup16g.todo.db.slick

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Todo.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Todo
   *  @param id Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param content Database column content SqlType(TEXT)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP) */
  case class TodoRow(id: Long, name: String, content: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching TodoRow objects using plain SQL queries */
  implicit def GetResultTodoRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TodoRow] = GR{
    prs => import prs._
    TodoRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table todo. Objects of this class serve as prototypes for rows in queries. */
  class Todo(_tableTag: Tag) extends profile.api.Table[TodoRow](_tableTag, Some("todo"), "todo") {
    def * = (id, name, content, createdAt, updatedAt) <> (TodoRow.tupled, TodoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(content), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> TodoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column content SqlType(TEXT) */
    val content: Rep[String] = column[String]("content")
    /** Database column created_at SqlType(TIMESTAMP) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(TIMESTAMP) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
  }
  /** Collection-like TableQuery object for table Todo */
  lazy val Todo = new TableQuery(tag => new Todo(tag))
}
