package models

import java.sql.Date
import play.api.libs.functional.syntax._
import play.api.libs.json._


  case class Task (id: Int, number: String, description: String, category: String, date: Date)

  object Task {
    implicit val communicationReads: Reads[Task] = (
      (JsPath \ "id").read[Int] and
        (JsPath \ "number").read[String] and
        ( (JsPath \ "description").read[String]) and
        ( (JsPath \ "category").read[String]) and
        ( (JsPath \ "date").read[Date])
      )(Task.apply _)


    implicit val communicationWrites: Writes[Task] = (
      (JsPath \ "id").write[Int] and
        ( (JsPath \ "number").write[String]) and
        (JsPath \ "description").write[String] and
        (JsPath \ "category").write[String] and
        ( (JsPath \ "date").write[Date])
      )(unlift(Task.unapply _))
  }


