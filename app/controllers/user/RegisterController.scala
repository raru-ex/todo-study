package net.syrup16g.todo.controllers.user

import play.api.mvc._
import javax.inject.{Singleton, Inject}
import play.api.i18n.I18nSupport

@Singleton
class RegisterController @Inject()(
  cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {

  /**
   * ログイン画面表示
   */
  def create() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.user.create())
  }

  def store() = Action { implicit request: Request[AnyContent] =>
    println("ok!!!!")
    Ok(views.html.user.create())
  }
}
