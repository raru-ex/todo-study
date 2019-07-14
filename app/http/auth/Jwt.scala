package net.syrup16g.todo.http.auth

import play.api.Configuration
import play.api.libs.json._
import javax.inject.{Singleton, Inject}
import javax.crypto.Cipher
import org.apache.commons.codec.binary.Base64
import play.api.ConfigLoader
import com.typesafe.config.Config
import com.typesafe.config.ConfigList
import scala.math._
import java.security.{KeyFactory, Key}
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.{ Base64 => jBase64 }
import java.nio.charset.StandardCharsets
import org.apache.commons.codec.Charsets

/**
 * Jwt処理用クラス
 */
case class Jwt(
  header:    JwtHeader,
  claim:     JwtClaim,
  signature: String
) extends JwtConfig with JwtConverter {

  /**
   * 正しいJwtか検証
   */
  def isValid()(implicit conf: Configuration): Boolean = {
    signature == Base64.encodeBase64URLSafeString(
      encryptWithRSA(header.getKey, (jsonToBase64(header.toJson) + "." + jsonToBase64(claim.toJson)).getBytes))
  }


  /**
   * jwtのエンコード文字列を作成
   */
  def encode()(implicit conf: Configuration): String = {
    val encodedHeader = jsonToBase64(header.toJson)
    val encodedClaim  = jsonToBase64(claim.toJson)
    val signatureSeed = encodedHeader + "." + encodedClaim
    val signature     = Base64.encodeBase64URLSafeString(encryptWithRSA(header.getKey(), signatureSeed.getBytes))
    signatureSeed + "." + signature
  }

}

/**
 * Jwtコンパニオンオブジェクト
 */
object Jwt {

  /**
   * Jwt文字列からJwtクラスを作成
   */
  def apply(jwtString: String)(implicit conf: Configuration): Jwt = {
    val parts = splitJwt(jwtString)

    new Jwt(
      JwtHeader(new String(Base64.decodeBase64(parts._1), Charsets.UTF_8)),
      JwtClaim (new String(Base64.decodeBase64(parts._2), Charsets.UTF_8)),
      parts._3
    )
  }

  /**
   * Jwt文字列を分割
   */
  private def splitJwt(jwtString: String): (String, String, String) = {
    // header, claim, signatureに分割
    val parts = jwtString.split("\\.")

    parts.length match {
      case 3 => (parts(0), parts(1), parts(2))
      case _ => throw new Exception("invalid jwt format")
    }
  }
}


