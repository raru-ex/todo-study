package net.syrup16g.todo.controllers

import play.api.mvc._
import javax.inject.{Singleton, Inject}
import play.api.i18n.I18nSupport
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import net.syrup16g.todo.repositories.UserRepository
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import net.syrup16g.todo.form.UserForm
import net.syrup16g.todo.http.auth.JwtEncoder
import play.api.libs.json._
import play.api.Configuration

@Singleton
class LoginController @Inject()(
  cc: MessagesControllerComponents,
  config: Configuration
) extends AbstractController(cc) with I18nSupport with UserForm {

  /**
   * ログイン画面表示
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    val encoder = new JwtEncoder(config)
    val jwt = encoder.encode("RS256", Json.obj("test" -> "test"))
        println(jwt)
        encoder.decode(jwt)
    Ok(views.html.login.index(loginForm))
  }

  /**
   * ログイン処理
   */
  def login() = Action async { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      errors => {
        Future.successful(BadRequest(views.html.login.index(errors)))
      },
      form => {
        // TODO: refactor. もう少し一つにまとめられる気がする
        val bcryptEncoder = new BCryptPasswordEncoder()
        for {
          userOpt <- UserRepository.findByMail(form.mail)
          } yield {
            val isAuthenticated = userOpt match {
              case Some(user) => bcryptEncoder.matches(form.password, user.password)
              case _          => false
            }

            // TODO: 認証系処理
            isAuthenticated match {
              case true  => Redirect("/")
              case false =>
                BadRequest(views.html.login.index(loginForm.withGlobalError("ログイン情報が正しくありません")))
            }
          }
      })
  }
}
