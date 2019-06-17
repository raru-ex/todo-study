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
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.{ Base64 => jBase64 }
import java.nio.charset.StandardCharsets

class JwtEncoder (
  val config: Configuration,
  val path:   String = "play.http.jwt.setting"
) {
  val defaultAlgorithm = "RS256"

  def encode(alg: String, data: JsObject): String = {
    val jwtKeySeq = config.get[Seq[JwtKeyConfiguration]](s"${path}.keys")
    val jwtCommon = config.get[JwtCommonConfiguration](s"${path}.common")
    val keyConfig = getKeyForSignaturea(jwtKeySeq, alg)

    val encodedHeader = Base64.encodeBase64URLSafeString(createHeader(keyConfig))
    val encodedClaim  = Base64.encodeBase64URLSafeString(createClaim(jwtCommon, data))
    val signatureSeed = encodedHeader + "." + encodedClaim
    val signature = Base64.encodeBase64URLSafeString(encryptWithRSA(keyConfig.key, signatureSeed.getBytes()))
    signatureSeed + "." + signature
  }

  /**
   * TODO: 整理
   */
  def decode(jwt: String) = try {
    val pubkeyStr = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAujcxG1uhew29u2NQUpRD\niTfv8peHgqGfX1CyKzgThvzelZ81s0I6Ni1CVk3O62Qo9bxsOwpd94EP15ZFxsFm\nsFHig/jyGIbxtrpgdDA4ndzOOArW4wGND9hGgTlH+qKiRjtozZpImq0hODTKuscl\nm5uTRq/DkKDbCTbZ6d3O17rvfEYjVq1ZHQ6WPqMrdriINtwNxIxqIfCl2FGvHAat\nxl1TIyJ1+Lwa0Elz/U0JXJE6XtzZZxFeyO8jVrNg0PK+EaHQ5IdcnhAOV0L4YJPE\ny+vUqlEF//dHiCRG50sMMTKa+KPcKKBlx6UyV0uMbvhmKleZiQlHWppIGvjBzGzA\nYQIDAQAB\n-----END PUBLIC KEY-----"
    println("=============================== split ================================")
    val parts = jwt.split("\\.")
    println("=============================== start decode ================================")
    println("=============================== header ================================")
    println(new String(Base64.decodeBase64(parts(0).getBytes), StandardCharsets.UTF_8))
    println("=============================== claim ================================")
    println(new String(Base64.decodeBase64(parts(1).getBytes), StandardCharsets.UTF_8))
    println("=============================== signature  ================================")
    val signatureSeed = new String(decryptWithRSA(pubkeyStr, Base64.decodeBase64(parts(2).getBytes)), StandardCharsets.UTF_8)
    println(signatureSeed)
    val seeds = signatureSeed.split("\\.")
    println("=============================== signature seed ================================")
    println(seeds)
    println("=============================== signature seed ================================")
    println(new String(Base64.decodeBase64(seeds(0).getBytes), StandardCharsets.UTF_8))
    println(new String(Base64.decodeBase64(seeds(1).getBytes), StandardCharsets.UTF_8))
  } catch {
    case e: Exception =>
      e.printStackTrace
      // TODO: 不正な操作です系エラーに置き換え
      throw new Exception("invalid jwt format")
  }


  private def encryptWithRSA(key: String, target: Array[Byte]): Array[Byte] = {
    val r = s"-{5}.*-{5}".r
    val keyContent = r.replaceAllIn(key, "").replaceAll("\\n", "")
    println(keyContent)
    val kf = KeyFactory.getInstance("RSA")
    val privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(jBase64.getDecoder.decode(keyContent)))

    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, privateKey)
    cipher.doFinal(target)
  }


  private def decryptWithRSA(key: String, target: Array[Byte]): Array[Byte] = {
    val r = s"-{5}.*-{5}".r
    val keyContent = r.replaceAllIn(key, "").replaceAll("\\n", "")
    val kf = KeyFactory.getInstance("RSA")
    val publicKey = kf.generatePublic(new X509EncodedKeySpec(jBase64.getDecoder.decode(keyContent)))

    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, publicKey)
    cipher.doFinal(target)
  }

  private def createHeader(config: JwtKeyConfiguration) = Json.obj(
    "alg" -> config.alg,
    "typ" -> config.typ
  ).toString.getBytes

  private def createClaim(commonConfig: JwtCommonConfiguration, data: JsObject) = {
    Json.obj(
      "iss" -> commonConfig.iss
      ).deepMerge(data).toString.getBytes
  }

  private def getKeyForSignaturea(confSeq: Seq[JwtKeyConfiguration], alg: String = defaultAlgorithm) = {
    val filteredConfSeq = confSeq.filter(_.alg == alg)

    filteredConfSeq.isEmpty match {
      case false =>
        val maxIndex = filteredConfSeq.size - 1
        filteredConfSeq(floor(random * maxIndex).toInt)
      case true  =>
        throw new Exception("there is no jwt key setting.")
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
    def load(rootConfig: Config, path: String): Seq[JwtKeyConfiguration] = {
      import scala.collection.JavaConverters._

      val configList = rootConfig.getConfigList(path).asScala
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
    def load(rootConfig: Config, path: String): JwtCommonConfiguration = {
      val config = rootConfig.getConfig(path)
      JwtCommonConfiguration(
        iss = config.getString("issuer")
      )
    }
  }
}
