package net.syrup16g.todo.http

import javax.inject.Inject
import play.api.UsefulException
import play.api.http.{DefaultHttpErrorHandler, MediaRange}
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

/**
  * 基本的なカスタムエラーハンドラ
  * いくつかのon系イベントや環境ごとのイベントが用意されている
  * 個別作成のクラスなので、Http用ハンドラと、Handler呼び出し切り分け用のクラスは作らない(play2.7を使えばあるのと面倒だから)
  */
class MyHtmlOrJsonErrorHandler @Inject() (
  jsonErrorHandler: MyJsonErrorHandler
) extends DefaultHttpErrorHandler {
  val CONTENT_TYPE_JSON = "application/json"

  /**
    * クライアントエラー(400系)のハンドラ
    */
  override def onClientError(request:  RequestHeader, statusCode:  Int, message: String): Future[Result] = {
    println("========== called onClientError ==========")
    println(request.acceptedTypes)

    // html, jsonしか想定してない前提で処理を分岐
    preferredMediaTypeName(request, CONTENT_TYPE_JSON) match {
      case Some(_) => jsonErrorHandler.onClientError(request, statusCode, message)
      case None => onHtmlClientError(request, statusCode, message)
    }
  }

  /**
    * Html用のクライアントエラー処理
    */
  private def onHtmlClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    super.onClientError(request, statusCode, message)
  }

  /**
    * サーバエラー(500系)のハンドラ
    */
  override def onServerError(request:  RequestHeader, exception: Throwable): Future[Result] = {
    println("========== called onServerError ==========")
    println(request.acceptedTypes)

    // html, jsonしか想定してない前提で処理を分岐
    preferredMediaTypeName(request, CONTENT_TYPE_JSON) match {
      case Some(_) => jsonErrorHandler.onServerError(request, exception)
      case None => onHtmlServerError(request, exception)
    }
  }

  /**
    * Html用のサーバエラー
    */
  private def onHtmlServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    super.onServerError(request, exception)
  }

  override def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    println("========== called onDevServerError ==========")
    super.onProdServerError(request, exception)
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    println("========== called onProdServerError ==========")
    super.onProdServerError(request, exception)
  }

  private def preferredMediaTypeName(request: RequestHeader, mediaType: String*): Option[String] =
    MediaRange.preferred(request.acceptedTypes, mediaType)

}
