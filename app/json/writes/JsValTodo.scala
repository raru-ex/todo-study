package net.syrup16g.todo.json.writes

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class JsValTodo(
  id: Long,
  name: String,
  content: String
)

object JsValTodo {
  //  implicit lazy val writes = Json.writes[Todo]

  implicit lazy val writes = new Writes[JsValTodo] {
    override def writes(todo: JsValTodo): JsValue =
      Json.obj(
        "id" -> todo.id,
        "name" -> todo.name,
        "content" -> todo.content,
      )
  }

  def apply(id: Long, name: String, content: String): JsValTodo = new JsValTodo(id, name, content)
}