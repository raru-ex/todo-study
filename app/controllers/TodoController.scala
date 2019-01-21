package net.syrup16g.todo.controllers

import slick.jdbc.MySQLProfile.api._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import net.syrup16g.todo.db.slick.Tables._
import play.api.libs.json.Json
import net.syrup16g.todo.json.JsValTodo
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class TodoController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
    * DBからTODOを全件取得して返す
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    //TODO: 毎回呼び出すのはアホらしいのでRepositoryにするなど、まとめる
    val db = Database.forConfig("slick.dbs.default.db")
    val tableQuery = TableQuery[Todo]
    val result = db.run {
      tableQuery.sortBy(_.id.asc).result
    }

    //TODO: Futureからの値の取り出し方が下手な気がするので、いいのを見つける
    val json = Await.result(result, Duration.Inf) map { todo =>
      Json.toJson(JsValTodo(todo.id, todo.name, todo.content))
    }

    //TODO: Okとか書かないといけないのか。だるいので何か探す
    Ok(
      Json.obj(
        "rows" -> json
      )
    )
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
