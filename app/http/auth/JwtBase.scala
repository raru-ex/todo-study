package net.syrup16g.todo.http.auth

import play.api.Configuration
import scala.math._
import play.api.ConfigLoader
import com.typesafe.config.Config

/**
 * Jwtに必要なconfを処理するクラス
 */
trait JwtConfig {
  protected val defaultAlgorithm = "RS256"
  protected val configPath = "play.http.jwt.setting"

  /**
   * confファイルから鍵一覧を取得
   */
  def getKeyConfigs()(implicit config: Configuration): Seq[JwtKeyConfiguration] = config.get[Seq[JwtKeyConfiguration]](s"${configPath}.keys")

  /**
   * confファイルから共通設定を取得
   */
  def getKeyCommon()(implicit config: Configuration): JwtCommonConfiguration = config.get[JwtCommonConfiguration](s"${configPath}.common")

  /**
   * confファイルからランダムに鍵を取得する
   */
  def getKeyAtRandom()(implicit conf: Configuration): JwtKeyConfiguration = getKeyByAlgAtRandom(defaultAlgorithm)

  /**
   * confファイルから対象のアルゴリズムの鍵をランダムに取得する
   */
  def getKeyByAlgAtRandom(alg: String)(implicit conf: Configuration): JwtKeyConfiguration = {
    val confSeq = getKeyConfigs()
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

/**
 * Jwt関連のconf設定を受け取るクラス
 */
case class JwtKeyConfiguration (
  kid:     String,
  typ:     String = "JWT",
  alg:     String = "RS256",
  key:     String = "key",
  expires: Long = 60 * 30,
)

/**
 * JwtKeyConfigurationのLoader
 */
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

/**
 * Jwt関連の共通設定を受け取るクラス
 */
case class JwtCommonConfiguration(iss: String)

/**
 * JwtCommonConfigurationのLoader
 */
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
