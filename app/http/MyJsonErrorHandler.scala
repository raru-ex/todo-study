package net.syrup16g.todo.http

import javax.inject.Inject
import play.api.{Environment, Mode, OptionalSourceMapper}
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandlerExceptions}
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Result, Results}
import play.core.SourceMapper

import scala.concurrent.Future

/**
  * Json用エラーハンドラ
  */
class MyJsonErrorHandler(
  environment: Environment,
  sourceMapper: Option[SourceMapper] = None)
extends DefaultHttpErrorHandler {

  @Inject()
  def this(environment: Environment, optionalSourceMapper: OptionalSourceMapper) = {
    this(environment, optionalSourceMapper.sourceMapper)
  }

  /**
    * クライアントエラー(400系)のハンドラ
    */
  override def onClientError(request:  RequestHeader, statusCode:  Int, message: String): Future[Result] = {
    println("========== called json onClientError ==========")
    println(request.acceptedTypes)
    println(message)

    Future.successful(Results.Status(statusCode)(Json.obj("error" -> Json.obj(
      "code"    -> statusCode,
      "message" -> message
    ))))
  }

  /**
    * サーバエラー(500系)のハンドラ
    */
  override def onServerError(request:  RequestHeader, exception: Throwable): Future[Result] = {
    println("========== called json onServerError ==========")
    val isProd = environment.mode == Mode.Prod
    val usefulException = HttpErrorHandlerExceptions.throwableToUsefulException(
        sourceMapper,
        isProd,
        exception
      )
    exception.printStackTrace()

    Future.successful(Results.InternalServerError(Json.obj("error" -> Json.obj(
      "code"    -> "BE001",
      "id" -> usefulException.id,
      "requestId" -> request.id,
      "title" -> usefulException.title,
      "description" -> usefulException.description,
      "stacktrace" -> usefulException.cause.toString
    ))))
  }
}
