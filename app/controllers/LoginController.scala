package net.syrup16g.todo.controllers

import play.api.mvc._
import javax.inject.{Singleton, Inject}

@Singleton
class LoginController @Inject()(
  cc: ControllerComponents
) extends AbstractController(cc) {

  /**
   * ログイン画面表示
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login.index())
  }

  def login() = ???
}
