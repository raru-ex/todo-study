package net.syrup16g.todo.controllers.user

import play.api.mvc._
import javax.inject.{Singleton, Inject}
import play.api.i18n.I18nSupport
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import net.syrup16g.todo.repositories.UserRepository
import net.syrup16g.todo.db.model.User
import scala.concurrent.Future
import net.syrup16g.todo.form.UserForm._
import scala.concurrent.ExecutionContext

@Singleton
class RegisterController @Inject()(
  cc:             MessagesControllerComponents,
  userRepository: UserRepository
)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  /**
   * 画面表示
   */
  def create() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.user.create(signUpForm))
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    signUpForm.bindFromRequest.fold(
      errors => {
        Future.successful(BadRequest(views.html.user.create(errors)))
      },
      form => {
        val bcryptEncoder = new BCryptPasswordEncoder()
        val password      = bcryptEncoder.encode(form.password.main)
        val newUser       = User(None, form.nickname, form.mail, password)
        for {
          idOpt <- userRepository.insert(newUser)
        } yield Redirect(routes.RegisterController.create())
      }
      )
  }
}
