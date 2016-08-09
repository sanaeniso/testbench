package models


import java.sql.{Date}
import java.time.LocalDateTime

import slick.driver.SQLiteDriver.api._
import slick.lifted.Tag

class Messages (tag: Tag) extends Table[Message](tag, "MESSAGES") {
  def id = column [Int]("id", O.PrimaryKey)
  def message = column[String]("message")
  def sender_uuid = column[String]("sender_uuid")
  def sender_name = column[String]("sender_name")
  def date = column[Date]("date")

  def * = (id, message, sender_uuid, sender_name, date) <> ((Message.apply _).tupled, Message.unapply _)
}
