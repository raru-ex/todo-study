package net.syrup16g.http

import play.api.http.DefaultHttpErrorHandler

class CustomHttpErrorHandler extends DefaultHttpErrorHandler {
  override def onClientError(request:  _root_.play.api.mvc.RequestHeader, statusCode:  Int, message:  _root_.scala.Predef.String): _root_.scala.concurrent.Future[_root_.play.api.mvc.Result] = {
    println("========== called onClientError ==========")
    super.onClientError(request, statusCode, message)
  }

  override def onServerError(request:  _root_.play.api.mvc.RequestHeader, exception:  scala.Throwable): _root_.scala.concurrent.Future[_root_.play.api.mvc.Result] = {
    println("========== called onServerError ==========")
    super.onServerError(request, exception)
  }

  override def onDevServerError(request:  _root_.play.api.mvc.RequestHeader, exception:  _root_.play.api.UsefulException): _root_.scala.concurrent.Future[_root_.play.api.mvc.Result] = {
    println("========== called onDevServerError ==========")
    super.onProdServerError(request, exception)
  }

  override def onProdServerError(request:  _root_.play.api.mvc.RequestHeader, exception:  _root_.play.api.UsefulException): _root_.scala.concurrent.Future[_root_.play.api.mvc.Result] = {
    println("========== called onProdServerError ==========")
    super.onProdServerError(request, exception)
  }
}
