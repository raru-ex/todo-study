package net.syrup16g.http

import javax.inject.Inject
import play.api.http._

/**
  * HtmlOrJsonHttpErrorHandlerと同じような実装
  * 渡されたMIMEに対して紐づけられたハンドラが呼び出される
  */
class CustomContentTypeErrorHandler @Inject() (
  jsonHandler: JsonHttpErrorHandler,
  htmlHandler: DefaultHttpErrorHandler,
  xmlHandler: XmlHttpErrorHandler
) extends PreferredMediaTypeHttpErrorHandler (
  "application/json" -> jsonHandler,
  "text/html"        -> htmlHandler,
  "application/xml"  -> xmlHandler
)
