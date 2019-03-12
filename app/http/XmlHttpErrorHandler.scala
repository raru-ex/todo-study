package http

import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class XmlHttpErrorHandler extends HttpErrorHandler {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = ???

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = ???
}
