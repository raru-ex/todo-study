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

/**
 * Jwt処理用クラス
 * TODO: カオスってるのでリファクタ
 */
case class Jwt (
  header:    JsObject,
  claim:     JsObject,
  signature: String
)(
  implicit val config: Configuration
) {

  /**
   * claimからuserIdを取得
   */
  def getUserId: Long = (claim \ "user_id").as[Long]

  /**
   * 正しいJwtか検証
   */
  def isValid: Boolean = {
    val kid = (this.header \ "kid").as[String]
    val key = Jwt.getKeyByKid(kid)

    signature == Base64.encodeBase64URLSafeString(
      Jwt.encryptWithRSA(key, (header + "." + claim).getBytes))
  }
}

object Jwt {
  val defaultAlgorithm = "RS256"
  val configPath       = "play.http.jwt.setting"

  /**
   * Jwt文字列からJwtクラスを作成
   */
  def apply(jwtString: String)(implicit conf: Configuration): Jwt = {
    val parts = splitJwt(jwtString)

    new Jwt(
      Json.parse(Base64.decodeBase64(parts._1.getBytes)).as[JsObject],
      Json.parse(Base64.decodeBase64(parts._2.getBytes)).as[JsObject],
      parts._3
    )
  }

  /**
   * RSA256でデータをjwtへエンコード
   */
  def encode(data: JsObject)(implicit conf: Configuration): String = encode(defaultAlgorithm, data)

  /**
   * 指定されたアルゴリズムでデータをjwtへエンコードする
   * TODO: RSAのみにしか対応していない
   */
  def encode(alg: String, data: JsObject)(implicit conf: Configuration): String = {
    val jwtKeySeq = getKeyConfigs
    val jwtCommon = getKeyCommon
    val keyConfig = getKeyForSignature(jwtKeySeq, alg)

    val encodedHeader = jsonToBase64(createHeader(keyConfig))
    val encodedClaim  = jsonToBase64(createClaim(jwtCommon, data))
    val signatureSeed = encodedHeader + "." + encodedClaim
    val signature     = Base64.encodeBase64URLSafeString(Jwt.encryptWithRSA(keyConfig.key, signatureSeed.getBytes))
    signatureSeed + "." + signature
  }

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

  /**
   * JWTのHeaderを作成する
   */
  private def createHeader(config: JwtKeyConfiguration) = Json.obj(
    "alg" -> config.alg,
    "typ" -> config.typ,
    "kid" -> config.kid
  )

  /**
   * JWTのデータ部分を作成する
   */
  private def createClaim(commonConfig: JwtCommonConfiguration, data: JsObject) = {
    Json.obj(
      "iss" -> commonConfig.iss
      ).deepMerge(data)
  }

  /**
   * jsonをBase64 encodeする
   */
  private def jsonToBase64(json: JsObject): String = Base64.encodeBase64URLSafeString(json.toString.getBytes)

  /**
   * 設定ファイルから対象のアルゴリズムの鍵を取得する
   */
  private def getKeyForSignature(confSeq: Seq[JwtKeyConfiguration], alg: String = defaultAlgorithm): JwtKeyConfiguration = {
    val filteredConfSeq = confSeq.filter(_.alg == alg)

    filteredConfSeq.isEmpty match {
      case false =>
        val maxIndex = filteredConfSeq.size - 1
        filteredConfSeq(floor(random * maxIndex).toInt)
      case true  =>
        throw new Exception("there is no jwt key setting.")
    }
  }

  /**
   * jwtを検証する
   */
  def validate(jwtString: String)(implicit config: Configuration): Boolean = {
    val parts = splitJwt(jwtString)

    val headerJson = Json.parse(Base64.decodeBase64(parts._1.getBytes))

    // jwtに利用されている鍵を取得
    val kid = (headerJson \ "kid").as[String]
    val key = getKeyByKid(kid)

    parts._3 == Base64.encodeBase64URLSafeString(
      encryptWithRSA(key, (parts._1 + "." + parts._2).getBytes))
  }

  private def getKeyByKid(kid: String)(implicit conf: Configuration): String = {
    val keyConfigSeq = getKeyConfigs
    keyConfigSeq.filter(_.kid == kid).headOption match {
      case Some(conf) => conf.key
      case None       => ""
    }
  }

  /**
   * RSA256を暗号化する
   * TODO: private keyを利用した暗号化しか検証していない
   */
  private def encryptWithRSA(key: String, target: Array[Byte]) = {
    rsaCrypt(Cipher.ENCRYPT_MODE, key, target, { (kf, keyContent) =>
      kf.generatePrivate(new PKCS8EncodedKeySpec(jBase64.getDecoder.decode(keyContent)))
    })
  }

  /**
   * RSA256を復号化する
   * TODO: public keyを利用した復号化しか検証していない
   */
  private def decryptWithRSA(key: String, target: Array[Byte]) = {
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
   * confファイルから鍵一覧を取得
   */
  private def getKeyConfigs()(implicit config: Configuration): Seq[JwtKeyConfiguration] = config.get[Seq[JwtKeyConfiguration]](s"${configPath}.keys")

  /**
   * confファイルから共通設定を取得
   */
  private def getKeyCommon()(implicit config: Configuration): JwtCommonConfiguration = config.get[JwtCommonConfiguration](s"${configPath}.common")



  private def splitJwt(jwtString: String) = {
    // header, claim, signatureに分割
    val parts = jwtString.split("\\.")

    parts.length match {
      case 3 => (parts(0), parts(1), parts(2))
      case _ => throw new Exception("invalid jwt format")
    }
  }
}

case class JwtKeyConfiguration (
  kid:     String,
  typ:     String = "JWT",
  alg:     String = "RS256",
  key:     String = "key",
  expires: Long = 60 * 30,
)

object JwtKeyConfiguration {
  implicit val configLoader: ConfigLoader[Seq[JwtKeyConfiguration]] = new ConfigLoader[Seq[JwtKeyConfiguration]] {
    def load(rootConfig: Config, configPath: String): Seq[JwtKeyConfiguration] = {
      import scala.collection.JavaConverters._

      val configList = rootConfig.getConfigList(configPath).asScala
      configList.map { config =>
        JwtKeyConfiguration(
          kid     = config.getString("kid"),
          typ     = config.getString("type"),
          alg     = config.getString("algorithm"),
          key     = config.getString("key"),
          expires = config.getLong("expires")
        )
      }
    }
  }
}

case class JwtCommonConfiguration(iss: String)

object JwtCommonConfiguration {
  implicit val configLoader: ConfigLoader[JwtCommonConfiguration] = new ConfigLoader[JwtCommonConfiguration] {
    def load(rootConfig: Config, configPath: String): JwtCommonConfiguration = {
      val config = rootConfig.getConfig(configPath)
      JwtCommonConfiguration(
        iss = config.getString("issuer")
      )
    }
  }
}
