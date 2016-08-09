package models

import java.sql.{Date}
import java.time.{LocalDateTime}

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Message (id: Int, message: String, sender_uuid: String, sender_name: String, date: Date)

object Message {
  implicit val messageReads: Reads[Message] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "message").read[String] and
      ( (JsPath \ "sender_uuid").read[String]) and
      ( (JsPath \ "sender_name").read[String]) and
      ( (JsPath \ "date").read[Date])
    )(Message.apply _)


  implicit val messageWrites: Writes[Message] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "message").write[String] and
      ( (JsPath \ "sender_uuid").write[String]) and
      ( (JsPath \ "sender_name").write[String]) and
      ( (JsPath \ "date").write[Date])
    )(unlift(Message.unapply _))
}

