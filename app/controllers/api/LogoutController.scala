package net.syrup16g.todo.controllers.api

import play.api.mvc._
import javax.inject.{Singleton, Inject}
import play.api.i18n.I18nSupport
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import net.syrup16g.todo.repositories.UserRepository
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import net.syrup16g.todo.form.UserForm._
import net.syrup16g.todo.http.auth.{Jwt, JwtSession}
import play.api.libs.json._
import play.api.Configuration
import net.syrup16g.todo.http.auth.{JwtHeader, JwtClaim, JwtConverter}

@Singleton
class LogoutController @Inject()(
  cc: MessagesControllerComponents,
  implicit val config: Configuration
) extends AbstractController(cc)
  with I18nSupport {

  /**
   * ログイン処理
   */
  def logout() = Action { implicit request: Request[AnyContent] =>
    NoContent.discardingCookies(DiscardingCookie(JwtSession.getCookieName))
  }
}
