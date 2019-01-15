package net.syrup16g.todo.controllers

import slick.jdbc.MySQLProfile.api._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import net.syrup16g.todo.db.slick.Tables._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class TodoController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  def test() = Action { implicit request: Request[AnyContent] =>
    val db = Database.forConfig("slick.dbs.default.db")
    val tableQuery = TableQuery[Todo]
    val result1 = db.run {
      tableQuery.filter(_.id === 1L).result
    }
    val result2 = db.run {
      Todo.filter(_.id === 2L).result
    }

    Await.result(result1, Duration.Inf) foreach println
    Await.result(result2, Duration.Inf) foreach println

    Ok(views.html.index())
  }

}
