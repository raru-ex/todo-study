/**
  * Note:
  *    Reference of the source code from the following point of view.
  *  ・Depending on the function of ConfigLoader.
  *    Behavior of easily obtaining the value of specified type via type function of get function
  *
  *  https://github.com/playframework/playframework/blob/master/core/play/src/main/scala/play/api/http/HttpErrorHandler.scala
  *
  * Copyright (C) 2009-2019 Lightbend Inc. (https://www.lightbend.com).
  *   Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this project except in compliance with the License.
  * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
  *
  * Unless required by applicable law or agreed to in writing,
  * software distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  * either express or implied. See the License for
  * the specific language governing permissions and limitations under the License.
*/

package net.syrup16g.http

import javax.inject.Inject
import play.api._
import play.api.http.{HttpErrorHandler, HttpErrorHandlerExceptions}
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.mvc.Results.InternalServerError
import play.core.SourceMapper
import play.libs.exception.ExceptionUtils

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.xml.Elem

/**
  * JsonHttpErrorHandlerを参考にxml用のエラーハンドラを作成
  * @param environment
  * @param sourceMapper
  */
class XmlHttpErrorHandler (environment: Environment, sourceMapper: Option[SourceMapper] = None)
  extends HttpErrorHandler {
  private val logger: Logger = Logger.apply(classOf[XmlHttpErrorHandler])

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
      logServerError(request, usefulException)
      Future.successful(InternalServerError(
        <error>
          <id>{usefulException.id}</id>
          <requestId>{request.id}</requestId>
          <exception>
            <title>{usefulException.title}</title>
            <description>{usefulException.description}</description>
            <stacktrace>{formatServerErrorException(usefulException.cause)}</stacktrace>
          </exception>
        </error>
      ))
    } catch {
      case NonFatal(e) => Future.successful(InternalServerError)
    }
  }

  /**
    * エラーログ出力
    * playのソースを流用
    */
  private def logServerError(request: RequestHeader, usefulException: UsefulException): Unit = {
    // Logger.errorの呼び方はdeprecatedになっているため、呼び方を修正
    logger.error(
      """
        |
        |! @%s - Internal server error, for (%s) [%s] ->
        | """.stripMargin.format(usefulException.id, request.method, request.uri),
      usefulException
    )
  }

  /**
    * stacktraceの各行をxmlのlineタグにして返す
    */
  private def formatServerErrorException(exception: Throwable): Array[Elem] = {
    ExceptionUtils.getStackFrames(exception).map(s => <line>{s.trim}</line>)
  }
}
