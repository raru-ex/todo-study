package net.syrup16g.todo.db

import slick.codegen.SourceCodeGenerator

object SlickCodeGenerator extends App {
  //TODO: application.confから取得
  SourceCodeGenerator.run(
    profile = "slick.jdbc.MySQLProfile",
    jdbcDriver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://127.0.0.1/todo",
    outputDir = "./output",
    pkg = "net.syrup16g.todo.model.db",
    user = Some("todo"),
    password = Some("password"),
    ignoreInvalidDefaults = true
  )
}
