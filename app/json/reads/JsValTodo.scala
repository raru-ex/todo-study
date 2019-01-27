package net.syrup16g.todo.json.reads

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class JsValTodo(
  name: String,
  content: String
)

object JsValTodo {
  implicit val reads: Reads[JsValTodo] = (
    (__ \ "name").read[String] and
    (__ \ "content").read[String]
  )(JsValTodo.apply _)

  def apply(name: String, content: String): JsValTodo = new JsValTodo(name, content)
}