package net.syrup16g.todo.viewmodels
import play.api.data._
import play.api.data.Forms._

object UserViewModel {
  lazy val signUpForm = Form(
    tuple(
      "nickname" -> nonEmptyText(maxLength = 255),
      "mail"     -> nonEmptyText(maxLength = 255),
      "password" -> tuple(
        "main"     -> nonEmptyText,
        "confirm"  -> nonEmptyText
      ).verifying(passwords => passwords._1 == passwords._2),
    )
  )
}
