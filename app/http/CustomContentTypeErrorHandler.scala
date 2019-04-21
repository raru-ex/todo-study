package net.syrup16g.todo.http

import javax.inject.Inject
import play.api.http._

/**
  * HtmlOrJsonHttpErrorHandlerと同じような実装
  * 渡されたMIMEに対して紐づけられたハンドラが呼び出される
  */
class CustomContentTypeErrorHandler @Inject() (
  htmlHandler: DefaultHttpErrorHandler,
  jsonHandler: JsonHttpErrorHandler,
  xmlHandler: XmlHttpErrorHandler
) extends PreferredMediaTypeHttpErrorHandler (
  // headがdefaultHandlerとして利用されるためhtmlを最初に記載
  "text/html"        -> htmlHandler,
  "application/json" -> jsonHandler,
  "application/xml"  -> xmlHandler
)
