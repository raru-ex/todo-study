package net.syrup16g.todo.db.codegen

import slick.codegen.SourceCodeGenerator
object SlickCodeGenerator extends App {
  //TODO: application.confから取得
  SourceCodeGenerator.run(
    profile = "slick.jdbc.MySQLProfile",
    jdbcDriver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://localhost:3306/todo?useSSL=false&nullNamePatternMatchesAll=true",
    outputDir = "./output",
    pkg = "net.syrup16g.todo.model.db",
    user = Some("todo"),
    password = Some("password"),
    ignoreInvalidDefaults = true
  )

}
