package net.syrup16g.todo.http.auth

import play.api.libs.json.JsObject
import org.apache.commons.codec.binary.Base64
import javax.crypto.Cipher
import java.security.{KeyFactory, Key}
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.{ Base64 => jBase64 }
import play.api.Configuration
import java.nio.charset.StandardCharsets

trait JwtConverter {

  /**
   * RSA256を暗号化する
   * TODO: private keyを利用した暗号化しか検証していない
   */
  protected def encryptWithRSA(key: String, target: Array[Byte]) = {
    rsaCrypt(Cipher.ENCRYPT_MODE, key, target, { (kf, keyContent) =>
      kf.generatePrivate(new PKCS8EncodedKeySpec(jBase64.getDecoder.decode(keyContent)))
    })
  }

  /**
   * RSA256を復号化する
   * TODO: public keyを利用した復号化しか検証していない
   */
  protected def decryptWithRSA(key: String, target: Array[Byte]) = {
    rsaCrypt(Cipher.DECRYPT_MODE, key, target, { (kf, keyContent) =>
      kf.generatePublic(new X509EncodedKeySpec(jBase64.getDecoder.decode(keyContent)))
    })
  }

  /**
   * RSA256処理のための共通処理
   */
  private def rsaCrypt(mode: Int, key: String, target: Array[Byte], f: (KeyFactory, String) => Key) = {
    val r = s"-{5}.*-{5}".r
    val keyContent = r.replaceAllIn(key, "").replaceAll("\\n", "")

    val kf = KeyFactory.getInstance("RSA")
    val cipher = Cipher.getInstance("RSA")
    cipher.init(mode, f(kf, keyContent))
    cipher.doFinal(target)
  }

  /**
   * jsonをBase64 encodeする
   */
  protected def jsonToBase64(json: JsObject): String = Base64.encodeBase64URLSafeString(json.toString.getBytes)



  /**
   * 現状encodeの検証用に作っただけ
   * TODO: 整理
   */
  @deprecated("it is a method for testing", "1.0")
  def decode(jwt: String) = try {
    val pubkeyStr = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAujcxG1uhew29u2NQUpRD\niTfv8peHgqGfX1CyKzgThvzelZ81s0I6Ni1CVk3O62Qo9bxsOwpd94EP15ZFxsFm\nsFHig/jyGIbxtrpgdDA4ndzOOArW4wGND9hGgTlH+qKiRjtozZpImq0hODTKuscl\nm5uTRq/DkKDbCTbZ6d3O17rvfEYjVq1ZHQ6WPqMrdriINtwNxIxqIfCl2FGvHAat\nxl1TIyJ1+Lwa0Elz/U0JXJE6XtzZZxFeyO8jVrNg0PK+EaHQ5IdcnhAOV0L4YJPE\ny+vUqlEF//dHiCRG50sMMTKa+KPcKKBlx6UyV0uMbvhmKleZiQlHWppIGvjBzGzA\nYQIDAQAB\n-----END PUBLIC KEY-----"
    val parts = jwt.split("\\.")
    val signatureSeed = new String(decryptWithRSA(pubkeyStr, Base64.decodeBase64(parts(2).getBytes)), StandardCharsets.UTF_8)
    println(signatureSeed)
    val seeds = signatureSeed.split("\\.")
  } catch {
    case e: Exception =>
      e.printStackTrace
      // TODO: 不正な操作です系エラーに置き換え
      throw new Exception("invalid jwt format")
  }

}

object JwtConverter extends JwtConverter {

  def encode(header: JwtHeader, claim: JwtClaim)(implicit conf: Configuration): String = {
    val encodedHeader = jsonToBase64(header.toJson)
    val encodedClaim  = jsonToBase64(claim.toJson)
    val signatureSeed = encodedHeader + "." + encodedClaim
    val signature     = Base64.encodeBase64URLSafeString(encryptWithRSA(header.getKey(), signatureSeed.getBytes))
    signatureSeed + "." + signature
  }

}
