package net.syrup16g.todo.http.auth

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Configuration
import play.api.libs.json.Reads

/**
 * JwtのHeaderクラス
 */
case class JwtHeader(
  keyId: String,
  alg:   String,
  typ:   String
) {

  /**
   * jsonを作成
   */
  def toJson: JsObject = Json.obj(
    "kid" -> keyId,
    "alg" -> alg,
    "typ" -> typ
  )

  /**
   * confからkidでkeyを取得する
   */
  def getKey()(implicit conf: Configuration): String = {
    val keyConfigSeq = JwtHeader.getKeyConfigs()
    keyConfigSeq.filter(_.kid == keyId).headOption match {
      case Some(conf) => conf.key
      case None       => ""
    }
  }

}

/**
 * コンパニオンオブジェクト
 */
object JwtHeader extends JwtConfig {
  implicit val reads: Reads[JwtHeader] = (
    (__ \ "kid").read[String] and
    (__ \ "alg").read[String] and
    (__ \ "typ").read[String]
  ){ (kid, alg, typ) => JwtHeader(kid, alg, typ)}

  /**
   * confからHeaderを作成
   */
  def apply()(implicit conf: Configuration): JwtHeader = {
    val keyConfig = getKeyAtRandom()
    new JwtHeader(keyConfig.kid, keyConfig.alg, keyConfig.typ)
  }

  /**
   * 基本系
   */
  def apply(kid: String, alg: String, typ: String) = new JwtHeader(kid, alg, typ)

  /**
   * Json文字列からapply
   */
  def apply(jsonString: String): JwtHeader = {
    Json.parse(jsonString).as[JwtHeader]
  }
}

