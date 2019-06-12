package net.syrup16g.todo.repositories

import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.User
import scala.concurrent.{ExecutionContext, Future}
import net.syrup16g.todo.db.slick.UserSlickModel

object UserRepository extends UserSlickModel {

  def findAll: Future[Seq[User]] = DBAction { query =>
    query.sortBy(user => user.id.asc).result
  }

  def findByMail(mail: String): Future[Option[User]] = DBAction { _
    .filter(_.mail === mail)
    .result.headOption
  }

  def insert(user: User): Future[Option[Long]] = DBAction { query =>
    (query returning query.map(_.id)) += user
  }

  def update(user: User): Future[Int] = DBAction { _
    .filter(_.id === user.id.getOrElse(-1L))
    .update(user)
  }

  def delete(id: Long): Future[Int] = DBAction { _
    .filter(_.id === id)
    .delete
  }
}
