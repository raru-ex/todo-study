package net.syrup16g.todo.controllers

import slick.jdbc.MySQLProfile.api._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import net.syrup16g.todo.db.slick.Tables._
import play.api.libs.json.{JsValue, Json}
import net.syrup16g.todo.json.JsValTodo

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

@Singleton
class TodoController @Inject()(cc: ControllerComponents, implicit val ec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * DBからTODOを全件取得して返す
    */
  def index() = Action async { // asyncで非同期化(returnがfutureに)
    implicit request: Request[AnyContent] =>

      //TODO: 毎回呼び出すのはアホらしいのでRepositoryにするなど、まとめる
      val db = Database.forConfig("slick.dbs.default.db")
      val tableQuery = TableQuery[Todo]

      // flatmap, map展開なので最終的にはDBのreturnに引っ張られてFuture型
      for {
        result <- db.run(tableQuery.sortBy(_.id.asc).result)
      } yield {
        val jsons = result.map { todo =>
          Json.toJson(JsValTodo(todo.id, todo.name, todo.content))
        }

        // Future[Result]を返す必要があるためOKで型をResultに。
        Ok(Json.obj({
          "rows" -> jsons
        }))
      }
  }

  /**
    * slickの動作検証用アクション
    */
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
