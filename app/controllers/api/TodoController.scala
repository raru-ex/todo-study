package net.syrup16g.todo.controllers.api

import slick.jdbc.MySQLProfile.api._
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.libs.json.{JsError, Json}
import net.syrup16g.todo.repositories.TodoRepository
import net.syrup16g.todo.db.model.Todo

import scala.concurrent.{ExecutionContext, Future}
import net.syrup16g.todo.http.refiner.AuthedControllerComponents
import net.syrup16g.todo.http.refiner.AuthedController
import play.api.Configuration

@Singleton
class TodoController @Inject()(
  cc:             AuthedControllerComponents,
  conf:           Configuration,
  todoRepository: TodoRepository
)(
  implicit ec: ExecutionContext
) extends AuthedController(cc) {

  /**
    * DBからTODOを全件取得して返す
    */
  def index() = Action andThen Authenticate async { // asyncで非同期化(returnがfutureに)
    implicit request: Request[AnyContent] =>

      // flatmap, map展開なので最終的にはDBのreturnに引っ張られてFuture型
      for {
        result <- todoRepository.findAll
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
          Future.successful(BadRequest(Json.obj("status" ->"OK", "message" -> JsError.toJson(errors))))
        },
        todo => for {
          _ <- todoRepository.insert(Todo(None, 1L, todo.name, todo.content))
        } yield NoContent
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
        jsvalTodo =>
          for {
            _ <- todoRepository.update(Todo(Some(id), 1L, jsvalTodo.name, jsvalTodo.content))
          } yield NoContent
      )
  }

  /**
   * Todoを削除
   */
  def delete(id: Long) = Action async {
    implicit request =>
      for {
        _ <- todoRepository.delete(id)
      } yield NoContent
  }

}
