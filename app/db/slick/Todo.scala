//package net.syrup16g.todo.db.slick
//
//import java.sql.Timestamp
//
//import slick.jdbc.MySQLProfile.api._

//class Todo(tag: Tag) extends Table[(Long, String, String, Timestamp, Timestamp)](tag, "todo") {
//  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//  def name = column[String]("name")
//  def content = column[String]("content")
//  def createdAt = column[String]("created_at")
//  def updatedAt = column[String]("updated_at")
//
//  def * = (id, name, content, createdAt, updatedAt)
//}
//
