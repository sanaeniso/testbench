package controllers

import DAO.SharedDBConfig.dbConfig
import DAO.SharedDBConfig.dbConfig.driver.api._
import DAO.UsersRepository
import controllers.AuthenticatedUsers.isUserAuthenticated
import models.User
import models.User._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import  controllers.AuthenticatedUsers.unregisterUser
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class UserController extends Controller {

  private def validateRequest(request: Request[AnyContent], onAuthorized: Result, onUnauthorized: Result): Result =
    request.session.get("name") map { name =>
      if (isUserAuthenticated(name)) onAuthorized
      else onUnauthorized
    } getOrElse onUnauthorized

  def login = Action { request =>
    validateRequest(request, Ok(views.html.index()), Ok(views.html.login()))
  }

  def logout = Action { request =>
    request.session.get("name").foreach(name => unregisterUser(name))
    Ok(views.html.login())
  }

  def index = Action { request =>
    validateRequest(request, Ok(views.html.index()), Unauthorized( (views.html.auth())))
  }


    def getAllUsers = Action.async {
      val result = dbConfig.db.run(UsersRepository.allUsers)
      result.map(ContactList => Ok(Json.obj("users" -> Json.toJson(ContactList))))
    }

    def show(name: String) = Action.async {
      val user = dbConfig.db.run(UsersRepository.findByName(name))
      user.map(u => Ok(Json.obj("user" -> Json.toJson(u))))
    }


    def add = Action.async(BodyParsers.parse.json) { request =>
      val user = request.body.validate[User]
      user.fold(
        errors => Future(BadRequest(Json.obj(
          "status" -> "Parsing user failed",
          "error" -> JsError.toJson(errors)))),
        user => {
          val res = dbConfig.db.run(UsersRepository.exist(user.name, user.password))
          val exitsUserInDataBase = Await.result(res, 5.seconds)

          if (!exitsUserInDataBase)
            dbConfig.db.run(UsersRepository.users += user).map(u => Ok(Json.obj("status" -> "Ok", "user" -> Json.toJson(u))))
          else
            Future(Ok(Json.obj("status" -> "NOT Ok")))
        }
      )
    }

    def update(id: Int) = Action.async(BodyParsers.parse.json) { request =>
      val user = request.body.validate[User]
      user.fold(
        errors => Future(BadRequest(Json.obj(
          "status" -> "User update failed",
          "error" -> JsError.toJson(errors)))),
        user => {
          dbConfig.db.run(UsersRepository.updateUser(user))
          Future(Ok(Json.obj("user" -> Json.toJson(user))))
        }
      )
    }

    def delete(name: String) = Action.async {
      dbConfig.db.run(UsersRepository.deleteUser(name)).map(user => Ok(Json.obj("status" -> "Ok")))
    }

  }


