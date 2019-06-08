package net.syrup16g.todo.repositories

import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.Todo
import scala.concurrent.{ExecutionContext, Future}
import net.syrup16g.todo.db.slick.TodoSlickModel

object TodoRepository extends TodoSlickModel {

  def findAll: Future[Seq[Todo]] = DBAction { query =>
    query.sortBy(todo => todo.id.asc).result
  }

  def insert(todo: Todo): Future[Option[Long]] = DBAction { query =>
    (query returning query.map(_.id)) += todo
  }

  def update(todo: Todo): Future[Int] = DBAction { _
    .filter(_.id === todo.id.getOrElse(-1L))
    .update(todo)
  }

  def delete(id: Long): Future[Int] = DBAction { _
    .filter(_.id === id)
    .delete
  }
}
