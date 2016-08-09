package DAO

import play.api.Play.current
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

object SharedDBConfig {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile]
}
