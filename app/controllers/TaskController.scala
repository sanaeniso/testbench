package controllers

import DAO.SharedDBConfig.dbConfig
import DAO.SharedDBConfig.dbConfig.driver.api._
import DAO.UsersRepository
import controllers.AuthenticatedUsers.isUserAuthenticated
import models.{Message, Messages, Task, Tasks}
import models.User._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import scala.concurrent.{Await, Future}


class TaskController extends Controller {
  val tasks = TableQuery[Tasks]
  def findQuery(id: Int) = tasks.filter(_.id === id).result

  private def validateRequest(request: Request[AnyContent], onAuthorized: Result, onUnauthorized: Result): Result =
    request.session.get("name") map { name =>
      if (isUserAuthenticated(name)) onAuthorized
      else onUnauthorized
    } getOrElse onUnauthorized


  def task = Action { request =>
    validateRequest(request, Ok(views.html.tasks()), Unauthorized((views.html.auth())))
  }


  def getAllTasks = Action.async {
    val result = dbConfig.db.run(tasks.result)
    result.map(tasks => Ok(Json.obj("status" -> "Ok", "tasks" -> Json.toJson(tasks))))
  }


  def addTask = Action.async(BodyParsers.parse.json) { request =>
    val task = request.body.validate[Task]
    task.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing task failed",
        "error" -> JsError.toJson(errors)))),
      task =>
        dbConfig.db.run(tasks += task).map(t =>
          Ok(Json.obj("status" -> "Success", "task" -> Json.toJson(t)))
        ))
  }


  def deleteTask(number: String) = Action.async {
    dbConfig.db.run(UsersRepository.deleteTask(number)).map(user => Ok(Json.obj("status" -> "Ok")))
  }


}