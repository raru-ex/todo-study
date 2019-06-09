package net.syrup16g.todo.db.model

import java.time.LocalDateTime

case class Todo(
  id: Option[Long],
  userId: Long,
  name: String,
  content: String,
  createdAt: LocalDateTime = LocalDateTime.now,
  updatedAt: LocalDateTime = LocalDateTime.now
)

