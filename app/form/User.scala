package net.syrup16g.todo.form
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints

// ========== Form Mapped case class
case class SignUp(nickname: String, mail: String, password: PasswordConfirm)
case class PasswordConfirm(main: String, confirm: String)

/**
 * User related From creator
 */
object UserForm {

  lazy val signUpForm: Form[SignUp] = Form(
    mapping(
      "nickname" -> nonEmptyText(maxLength = 255),
      "mail"     -> nonEmptyText.verifying(Constraints.emailAddress),
      "password" -> mapping(
          "main"    -> nonEmptyText,
          "confirm" -> nonEmptyText
        )(PasswordConfirm.apply)(PasswordConfirm.unapply).verifying(password => password.main == password.confirm)
      )(SignUp.apply)(SignUp.unapply)
    )
}
