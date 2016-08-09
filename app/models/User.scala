package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User (id: Int, login: String, name: String, email: String, password: String)

object User {

    implicit val userReads: Reads[User] = (
      (JsPath \ "id").read[Int] and
        (JsPath \ "login").read[String] and
        (JsPath \ "name").read[String] and
        (JsPath \ "email").read[String] and
        (JsPath \ "password").read[String]
      ) (User.apply _)

    implicit val userWrites: Writes[User] = (
      (JsPath \ "id").write[Int] and
        (JsPath \ "login").write[String] and
        (JsPath \ "name").write[String] and
        (JsPath \ "email").write[String] and
        (JsPath \ "password").write[String]
      ) (unlift(User.unapply))
}

