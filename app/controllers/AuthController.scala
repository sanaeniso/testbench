package controllers

import DAO.SharedDBConfig.dbConfig
import DAO.UsersRepository
import controllers.AuthenticatedUsers.registerUser
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Await
import scala.concurrent.duration._

class AuthController extends Controller {
  var connectedUsers: Map[Long, String] = Map()

  def authenticate(name: String, password: String)  = Action { request =>
    val res = dbConfig.db.run(UsersRepository.exist(name, password))
    val exitsUserInDataBase = Await.result(res, 5.seconds)
    if (exitsUserInDataBase) {
      registerUser(name)

      Ok(Json.obj("status" -> "Ok", "credentials" -> Json.toJson(true))).withSession("name" -> name)
    }
    else Ok(Json.obj("status" -> " NOT Ok", "credentials" -> Json.toJson(false)))
  }

}

