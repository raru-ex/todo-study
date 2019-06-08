package net.syrup16g.todo.db.model

import java.sql.Timestamp

case class User(
  id: Option[Long],
  nickname: String,
  mail: String,
  password: String,
  createdAt: java.sql.Timestamp = new Timestamp(System.currentTimeMillis()),
  updatedAt: java.sql.Timestamp = new Timestamp(System.currentTimeMillis())
)

