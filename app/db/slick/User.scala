package net.syrup16g.todo.db.slick

import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.User
import net.syrup16g.todo.db.slick.mapping.{MySQLDateTime, SlickTypeMapper}
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

trait UserSlickModel extends BaseQuery[UserTable] {
  val query = new TableQuery(tag => new UserTable(tag))
}

/**
 * UserTableの定義と紐付け
 */
class UserTable(_tableTag: Tag) extends Table[User](_tableTag, Some("todo"), "user") with SlickTypeMapper {

  def * = (id, nickname, mail, password, createdAt, updatedAt) <> (
    (v: (Option[Long], String, String, String, MySQLDateTime, MySQLDateTime)) =>
      User(v._1, v._2, v._3, v._4, v._5.toLocalDateTime, v._6.toLocalDateTime),
      (user: User) => User.unapply(user).map( v => (
        v._1, v._2, v._3, v._4,
        MySQLDateTime(v._5),
        MySQLDateTime(LocalDateTime.now)
      )
    )
  )

  /** Maps whole row to an option. Useful for outer joins. */
  def ? = ((Rep.Some(id), Rep.Some(nickname), Rep.Some(mail), Rep.Some(password), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4.get, _5.get.toLocalDateTime, _6.get.toLocalDateTime)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  val id = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
  val nickname = column[String]("nickname", O.Length(255,varying=true))
  val mail = column[String]("mail", O.Length(255,varying=true))
  val password = column[String]("password", O.Length(100,varying=true))
  val createdAt = column[MySQLDateTime]("created_at")
  val updatedAt = column[MySQLDateTime]("updated_at")

  val index1 = index("unique_idx_nickname", nickname, unique=true)
}
