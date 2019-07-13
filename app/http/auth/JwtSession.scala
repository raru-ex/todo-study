package net.syrup16g.todo.http.auth
import play.api.Configuration

case class JwtSession() {
}

object JwtSession {

  def getCookieName()(implicit conf: Configuration) = conf.getOptional[String]("http.play.cookie.name").getOrElse("csession")
}
