package net.syrup16g.todo.http.auth

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Configuration
import play.api.libs.json.Reads

/**
 * JwtのClaimクラス
 */
case class JwtClaim(
  iss:    String,
  userId: Long
) {

  /**
   * jsonを作成
   */
  def toJson: JsObject = Json.obj(
    "iss"     -> iss,
    "user_id" -> userId
  )
}

/**
 * コンパニオンオブジェクト
 */
object JwtClaim extends JwtConfig {
  implicit val reads: Reads[JwtClaim] = (
    (__ \ "iss")    .read[String] and
    (__ \ "user_id").read[Long]
  ){ (iss, uid) => JwtClaim(iss, uid) }


  /**
   * confからClaimを作成
   */
  def apply(userId: Long)(implicit conf: Configuration): JwtClaim = {
    val keyConfig = getKeyCommon()
    new JwtClaim(keyConfig.iss, userId)
  }

  /**
   * 基本系
   */
  def apply(iss: String, userId: Long): JwtClaim = new JwtClaim(iss, userId)

  /**
   * Json文字列からapply
   */
  def apply(jsonString: String): JwtClaim = {
    Json.parse(jsonString).as[JwtClaim]
  }

}
