package net.syrup16g.todo.db.slick.mapping
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class MySQLDateTime(value: String) {
  def toLocalDateTime: LocalDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

object MySQLDateTime {
  def apply(time: LocalDateTime): MySQLDateTime = MySQLDateTime(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
}
