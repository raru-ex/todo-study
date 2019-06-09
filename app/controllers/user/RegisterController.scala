package net.syrup16g.todo.controllers.user

import play.api.mvc._
import javax.inject.{Singleton, Inject}
import play.api.i18n.I18nSupport
import net.syrup16g.todo.viewmodels.UserViewModel.signUpForm
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import net.syrup16g.todo.repositories.UserRepository
import net.syrup16g.todo.db.model.User
import scala.concurrent.ExecutionContext.Implicits._

@Singleton
class RegisterController @Inject()(
  cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {

  /**
   * 画面表示
   */
  def create() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.user.create())
  }

  def store() = Action { implicit request: Request[AnyContent] =>
    signUpForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.user.create())
      },
      form => {
        val bcryptEncoder = new BCryptPasswordEncoder()
        val password = bcryptEncoder.encode(form._3._1)
        val newUser = User(None, form._1, form._2, password)
        for {
          idOpt <- UserRepository.insert(newUser)
        } yield NoContent
      })
    Ok(views.html.user.create())
  }
}
