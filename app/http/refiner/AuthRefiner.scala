package net.syrup16g.todo.http.refiner

import play.api.mvc._
import net.syrup16g.todo.repositories.UserRepository
import net.syrup16g.todo.db.model.User
import net.syrup16g.todo.http.auth.{JwtEncoder, JwtCookie}
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc.Results
import javax.inject.{Singleton, Inject}
import play.api.Configuration
import play.api.i18n.{Langs, MessagesApi}
import play.api.http.FileMimeTypes

class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)

class AuthRefiner @Inject()()(implicit ec: ExecutionContext, conf: Configuration) extends ActionRefiner[Request, UserRequest] with JwtCookie {
  protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {
    val jwtEncoder = new JwtEncoder(conf)
    request.cookies.get(COOKIE_KEY_NAME) match {
      case Some(jwt: Cookie) if jwtEncoder.authenticate(jwt.value) =>
        val userId = jwtEncoder.getUserId(jwt.value)
        for {
          userOpt <- UserRepository.find(userId)
        } yield {
          userOpt match {
            case Some(user) => Right(new UserRequest[A](user, request))
            case None       => Left(Results.Redirect("/login"))
          }
        }
      case None      =>
        Future.successful(Left(Results.Redirect("/login")))
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

class AuthedController @Inject()(acc: AuthedControllerComponents) extends AbstractController(acc)
