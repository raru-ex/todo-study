package net.syrup16g.todo.db.codegen

import slick.codegen.SourceCodeGenerator

object SlickCodeGenerator extends App {
  //TODO: application.confから取得
//  SourceCodeGenerator.run(
//    profile = "slick.jdbc.MySQLProfile",
//    jdbcDriver = "com.mysql.cj.jdbc.Driver",
//    url = "jdbc:mysql://localhost:3306/todo",
//    outputDir = "./output",
//    pkg = "net.syrup16g.todo.model.db",
//    user = Some("todo"),
//    password = Some("password"),
//    ignoreInvalidDefaults = true
//  )


  SourceCodeGenerator.main(
    Array(
      "slick.jdbc.MySQLProfile",
      "com.mysql.cj.jdbc.Driver",
      "jdbc:mysql://127.0.0.1:3306/todo",
      "./output",
      "net.syrup16g.todo.slick.model.db",
      "todo",
      "password"
    )
  )

}
