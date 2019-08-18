package net.syrup16g.todo.db.slick

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.AbstractTable

/**
 * メモ
 *  queryの生成まで行いたかったがtag => TableQuery(new Table(tag))がリクレクション以外で上手く生成できなくて断念
 */
trait BaseQuery[E <: AbstractTable[_]] extends DbConfig {
  /** Collection-like TableQuery object for table Todo */
  val query: TableQuery[E]

  def DBAction[R](f: TableQuery[E] => DBIOAction[R, NoStream, Nothing])(implicit ex: ExecutionContext): Future[R] = db.run(f(query))

}

/**
 * 設定ファイルから読み込めていないためあまり意味がない
 */
trait DbConfig {
  val db: Database =  Database.forConfig("slick.dbs.default.db")
}
