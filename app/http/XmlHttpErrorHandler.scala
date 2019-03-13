package http

import javax.inject.Inject
import play.api.{Environment, Mode, OptionalSourceMapper}
import play.api.http.{HttpErrorHandler, HttpErrorHandlerExceptions}
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.mvc.Results.InternalServerError
import play.core.SourceMapper

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * JsonHttpErrorHandlerを参考にxml用のエラーハンドラを作成
  * @param environment
  * @param sourceMapper
  */
class XmlHttpErrorHandler (environment: Environment, sourceMapper: Option[SourceMapper] = None)
  extends HttpErrorHandler {

  /**
    * 環境情報をInjectすることで開発環境、テスト環境で処理を分けることが可能
    * @param environment
    * @param optionalSourceMapper
    * @return
    */
  @Inject
  def this(environment: Environment, optionalSourceMapper: OptionalSourceMapper) = {
    this(environment, optionalSourceMapper.sourceMapper)
  }

  /**
    * 400系エラー発生時に呼び出される処理
    * @param request
    * @param statusCode
    * @param message
    * @return
    */
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    if (play.api.http.Status.isClientError(statusCode)) {
      Future.successful(Results.Status(statusCode)(
        <error>
          <requestId>{request.id}</requestId>
          <message>{message}</message>
        </error>
      ))
    } else {
      throw new IllegalArgumentException(
        s"onClientError invoked with non client error status code $statusCode: $message"
      )
    }
  }

  /**
    * 500系エラー発生時に呼び出される処理。
    * @param request
    * @param exception
    * @return
    */
  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    try {
      val isProd = environment.mode == Mode.Prod
      val usefulException = HttpErrorHandlerExceptions.throwableToUsefulException(
        sourceMapper,
        isProd,
        exception
      )

      Future.successful(InternalServerError(
        <error>
          <id>{usefulException.id}</id>
          <requestId>{request.id}</requestId>
          <exception>
            <title>{usefulException.title}</title>
            <description>{usefulException.description}</description>
            <stacktrace>{usefulException.cause}</stacktrace>
          </exception>
        </error>
      ))
    } catch {
      case NonFatal(e) => Future.successful(InternalServerError)
    }
  }
}
