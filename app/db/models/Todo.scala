package net.syrup16g.todo.db.model

import java.sql.Timestamp

case class Todo(
  id: Option[Long],
  userId: Long,
  name: String,
  content: String,
  createdAt: java.sql.Timestamp = new Timestamp(System.currentTimeMillis()),
  updatedAt: java.sql.Timestamp = new Timestamp(System.currentTimeMillis())
)

