package net.syrup16g.todo.controllers

import slick.jdbc.MySQLProfile.api._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import net.syrup16g.todo.db.slick.Tables._
import play.api.libs.json.{JsError, Json}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

@Singleton
class TodoController @Inject()(cc: ControllerComponents, implicit val ec: ExecutionContext)
  extends AbstractController(cc) {
  val DB_CONFIG = "slick.dbs.default.db"

  /**
    * DBからTODOを全件取得して返す
    */
  def index() = Action async { // asyncで非同期化(returnがfutureに)
    implicit request: Request[AnyContent] =>

      //TODO: 毎回呼び出すのはアホらしいのでRepositoryにするなど、まとめる
      val db = Database.forConfig(DB_CONFIG)
      val tableQuery = TableQuery[Todo]

      // flatmap, map展開なので最終的にはDBのreturnに引っ張られてFuture型
      for {
        result <- db.run(tableQuery.sortBy(_.id.asc).result)
      } yield {
        import net.syrup16g.todo.json.writes.JsValTodo
        val jsons = result.map { todo =>
          Json.toJson(JsValTodo(todo.id.getOrElse(-1), todo.name, todo.content))
        }

        // Future[Result]を返す必要があるためOKで型をResultに。
        Ok(Json.obj({
          "rows" -> jsons
        }))
      }
  }

  /**
    * 新しいTodoを追加
    */
  def create() = Action(parse.json) async {
    implicit request =>
      import net.syrup16g.todo.json.reads.JsValTodo
      val validate = request.body.validate[JsValTodo]
      validate.fold(
        errors => {
          // asyncのためfutureで囲う
          Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors))))
        },
        todo => {
          val db = Database.forConfig(DB_CONFIG)
          val tableQuery = TableQuery[Todo]
          for {
            _ <- db.run(
              // Todoの作り方間違ってる
              (tableQuery returning tableQuery.map(_.id)) += TodoRow(None, todo.name, todo.content)
            )
          } yield NoContent
        }
      )
  }

  def update(id: Long) = Action(parse.json) async {
    implicit request =>
      import net.syrup16g.todo.json.reads.JsValTodo
      val validate = request.body.validate[JsValTodo]
      validate.fold(
        errors => {
          Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors))))
        },
        jsvalTodo => {
          val db = Database.forConfig(DB_CONFIG)
          val tableQuery = TableQuery[Todo]
          for {
            _ <- db.run(
              tableQuery.filter(_.id === id)
                .update(TodoRow(Some(id), jsvalTodo.name, jsvalTodo.content))
            )
          } yield NoContent
        }
      )
  }

  def delete(id: Long) = Action async {
    implicit request =>
      val db = Database.forConfig(DB_CONFIG)
      val tableQuery = TableQuery[Todo]
      for {
        _ <- db.run(
          tableQuery.filter(_.id === id).delete
        )
      } yield NoContent
  }

}
