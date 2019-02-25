package net.syrup16g.todo.db.codegen

import slick.codegen.SourceCodeGenerator

object SlickCodeGenerator extends App {
  //TODO: application.confから取得

  val profile = "slick.jdbc.MySQLProfile"
  val driver  = "com.mysql.cj.jdbc.Driver"
  val url     = "jdbc:mysql://localhost:3306/todo?useSSL=false&nullNamePatternMatchesAll=true"
  val outputDir = "./output"
  val pkg       = "net.syrup16g.todo.db.slick"
  val user      = "todo"
  val password  = "password"
  SourceCodeGenerator.main(
    Array(profile, driver, url, outputDir, pkg, user, password)
  )

}
