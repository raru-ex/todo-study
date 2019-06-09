package net.syrup16g.todo.db.slick

import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.Todo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import net.syrup16g.todo.db.slick.mapping.{MySQLDateTime, SlickTypeMapper}

trait TodoSlickModel extends BaseQuery[TodoTable] {
  val query = new TableQuery(tag => new TodoTable(tag))
}

class TodoTable(tag: Tag) extends Table[Todo](tag, Some("todo"), "todo") with SlickTypeMapper {

  /**
   * モデルとslick間のマッピング
   */
  def * = (id, userId, name, content, createdAt, updatedAt) <> (
    // slick to model
    (v: (Option[Long], Long, String, String, MySQLDateTime, MySQLDateTime)) =>
      Todo(v._1, v._2, v._3, v._4, v._5.toLocalDateTime, v._6.toLocalDateTime),
    // model to slick
    (todo: Todo) => Todo.unapply(todo).map(v => (
      v._1, v._2, v._3, v._4,
      MySQLDateTime(v._5),
      MySQLDateTime(LocalDateTime.now)
    ))
  )

  /** Maps whole row to an option. Useful for outer joins. */
  def ? =
    (Rep.Some(id), Rep.Some(userId),  Rep.Some(name), Rep.Some(content), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>(
      {
        r=>import r._; _1.map(_=> Todo.tupled((_1.get, _2.get, _3.get, _4.get, _5.get.toLocalDateTime, _6.get.toLocalDateTime)))
      },
      (_:Any) =>  throw new Exception("Inserting into ? projection not supported.")
    )

  val id        = column[Option[Long]] ("id",   O.AutoInc, O.PrimaryKey)
  val userId    = column[Long]         ("user_id")
  val name      = column[String]       ("name", O.Length(100,varying=true))
  val content   = column[String]       ("content")
  val createdAt = column[MySQLDateTime]("created_at")
  val updatedAt = column[MySQLDateTime]("updated_at")
}
