package models

import java.sql.Date

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Communication (id: Int, name: String, message: String, date: Date)

object Communication {
  implicit val communicationReads: Reads[Communication] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      ( (JsPath \ "message").read[String]) and
      ( (JsPath \ "date").read[Date])
    )(Communication.apply _)


  implicit val communicationWrites: Writes[Communication] = (
    (JsPath \ "id").write[Int] and
      ( (JsPath \ "name").write[String]) and
      (JsPath \ "message").write[String] and
      ( (JsPath \ "date").write[Date])
    )(unlift(Communication.unapply _))
}

