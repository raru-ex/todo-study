package net.syrup16g.todo.db.migrate

import org.flywaydb.core.Flyway
import play.api.Configuration
import javax.inject.{Singleton, Inject}
import org.flywaydb.core.api.configuration.ClassicConfiguration
import com.typesafe.config.ConfigFactory

object FlywayMigrate extends App {

  // confからDB情報取得
  val dbConf = ConfigFactory.load().getConfig("slick.dbs.default.db")

  // flyway用の設定を作成
  val configuration = new ClassicConfiguration()
  configuration.setDataSource(dbConf.getString("url"), dbConf.getString("user"), dbConf.getString("password"))

  val flyway = new Flyway(configuration)
  flyway.migrate()
}

