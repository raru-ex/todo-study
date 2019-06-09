package net.syrup16g.todo.db.model

import java.time.LocalDateTime

case class User(
  id: Option[Long],
  nickname: String,
  mail: String,
  password: String,
  createdAt: LocalDateTime = LocalDateTime.now,
  updatedAt: LocalDateTime = LocalDateTime.now
)

