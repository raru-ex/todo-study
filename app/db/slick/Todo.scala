package net.syrup16g.todo.db.slick

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcActionComponent
import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.Todo

trait TodoSchema extends {
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  val DB_CONFIG = "slick.dbs.default.db"
  val db = Database.forConfig(DB_CONFIG)


  /** GetResult implicit for fetching TodoRow objects using plain SQL queries */
  implicit def GetResultTodoRow(
    implicit e0: GR[Long],
    e1: GR[String],
    e2: GR[java.sql.Timestamp]
  ): GR[Todo] = GR { prs =>
    import prs._
    Todo.tupled((
        <<[Option[Long]],
        <<[Long],
        <<[String],
        <<[String],
        <<[java.sql.Timestamp],
        <<[java.sql.Timestamp]
       ))
  }

  /** Collection-like TableQuery object for table Todo */
  lazy val TodoQuery = new TableQuery(tag => new TodoTable(tag))

  def DBAction[R](f: TableQuery[TodoTable] => DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(f(TodoQuery))

  /** TodoTable description of table todo. Objects of this class serve as prototypes for rows in queries. */
  class TodoTable(tag: Tag) extends Table[Todo](tag, Some("todo"), "todo") {
    def * = (id, userId, name, content, createdAt, updatedAt) <> (Todo.tupled, Todo.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId),  Rep.Some(name), Rep.Some(content), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> Todo.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Option[Long]] = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(BIGINT UNSIGNED) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column content SqlType(TEXT) */
    val content: Rep[String] = column[String]("content")
    /** Database column created_at SqlType(TIMESTAMP) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(TIMESTAMP) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
  }
}
