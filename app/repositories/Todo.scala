package net.syrup16g.todo.repositories

import net.syrup16g.todo.db.slick.TodoSchema
import slick.jdbc.MySQLProfile.api._
import net.syrup16g.todo.db.model.Todo
import scala.concurrent.{ExecutionContext, Future}

object TodoRepository extends TodoSchema {

  def findAll: Future[Seq[Todo]] = DBAction { query =>
    query.sortBy(todo => todo.id.asc).result
  }
}
