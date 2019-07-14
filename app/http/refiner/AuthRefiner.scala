package net.syrup16g.todo.http.refiner

import play.api.mvc._
import play.api.mvc.Results.Unauthorized
import net.syrup16g.todo.repositories.UserRepository
import net.syrup16g.todo.db.model.User
import net.syrup16g.todo.http.auth.{Jwt, JwtSession}
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc.Results
import javax.inject.{Singleton, Inject}
import play.api.Configuration
import play.api.i18n.{Langs, MessagesApi}
import play.api.http.FileMimeTypes
import org.springframework.security.web.authentication.session.SessionAuthenticationException

class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)

class AuthRefiner @Inject()()(implicit ec: ExecutionContext, implicit val conf: Configuration) extends ActionRefiner[Request, UserRequest] {

  protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {

    request.cookies.get(JwtSession.getCookieName()) match {
      case Some(jwtCookie: Cookie) =>
        val jwt    = Jwt(jwtCookie.value)
        val userId = jwt.claim.userId
        for {
          userOpt <- UserRepository.find(userId)
        } yield {
          (jwt.isValid, userOpt) match {
            case (true, Some(user)) => Right(new UserRequest[A](user, request))
            case (_,    _)          => Left(Unauthorized("Authentication is invalid"))
          }
        }
      case None      =>
       Future.successful(Left(Unauthorized("Authentication is invalid")))
    }
  }

  override def executionContext: ExecutionContext = ec
}


case class AuthedControllerComponents @Inject()(
  authRefiner: AuthRefiner,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents

class AuthedController @Inject()(acc: AuthedControllerComponents) extends AbstractController(acc) {
  def Authenticate: AuthRefiner = acc.authRefiner
}
