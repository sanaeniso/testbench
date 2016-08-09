package models


import java.sql.{Date}
import java.time.LocalDateTime

import slick.driver.SQLiteDriver.api._
import slick.lifted.Tag

class Communications (tag: Tag) extends Table[Communication](tag, "communication") {
  def id = column [Int]("id", O.PrimaryKey)
  def name = column[String]("name")
  def message = column[String]("message")
  def date = column[Date]("date")

  def * = (id, name, message, date) <> ((Communication.apply _).tupled, Communication.unapply _)
}
