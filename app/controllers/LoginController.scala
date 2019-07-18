package net.syrup16g.todo.controllers

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
import play.filters.csrf.CSRFAddToken
import play.filters.csrf.CSRFCheck

@Singleton
class LoginController @Inject()(
  cc: MessagesControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  implicit val config: Configuration
) extends AbstractController(cc)
  with I18nSupport {

  /**
   * ログイン画面表示
   */
  def index() = addToken {
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.login.index(loginForm))
    }
  }

  /**
   * ログイン処理
   */
  def login() = checkToken {
    Action async { implicit request: Request[AnyContent] =>
      loginForm.bindFromRequest.fold(
        errors => {
          Future.successful(BadRequest(views.html.login.index(errors)))
        },
        form => {
          val bcryptEncoder = new BCryptPasswordEncoder()
          for {
            userOpt <- UserRepository.findByMail(form.mail)
            } yield {
              userOpt match {
                case Some(user) if bcryptEncoder.matches(form.password, user.password) =>
                  Redirect("/").withCookies(
                    new Cookie(
                      JwtSession.getCookieName(),
                      JwtConverter.encode(JwtHeader(), JwtClaim(user.id.get))
                    ))
                case _          =>
                  BadRequest(views.html.login.index(loginForm.withGlobalError("ログイン情報が正しくありません")))
              }
            }
        })
    }
  }
}
