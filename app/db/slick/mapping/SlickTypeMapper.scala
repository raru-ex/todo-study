package net.syrup16g.todo.db.slick.mapping

import slick.jdbc.MySQLProfile.api._
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait SlickTypeMapper {
  implicit lazy val timestampMapper = MappedColumnType.base[MySQLDateTime, Timestamp] (
    { l => Timestamp.valueOf(l.value) },
    { t => MySQLDateTime(t.toLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) }
  )
}
