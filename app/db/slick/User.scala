package net.syrup16g.todo.db.slick

import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.User

trait UserSlickModel extends BaseQuery[UserTable] {
  val query = new TableQuery(tag => new UserTable(tag))
}

class UserTable(_tableTag: Tag) extends Table[User](_tableTag, Some("todo"), "user") {
    def * = (id, nickname, mail, password, createdAt, updatedAt) <> (User.tupled, User.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(nickname), Rep.Some(mail), Rep.Some(password), Rep.Some(createdAt), Rep.Some(updatedAt))).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT UNSIGNED), AutoInc, PrimaryKey */
    val id: Rep[Option[Long]] = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
    /** Database column nickname SqlType(VARCHAR), Length(255,true) */
    val nickname: Rep[String] = column[String]("nickname", O.Length(255,varying=true))
    /** Database column mail SqlType(VARCHAR), Length(255,true) */
    val mail: Rep[String] = column[String]("mail", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(100,true) */
    val password: Rep[String] = column[String]("password", O.Length(100,varying=true))
    /** Database column created_at SqlType(TIMESTAMP) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at SqlType(TIMESTAMP) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")

    /** Uniqueness Index over (nickname) (database name unique_idx_nickname) */
    val index1 = index("unique_idx_nickname", nickname, unique=true)
  }
