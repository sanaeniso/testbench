package controllers

import DAO.SharedDBConfig.dbConfig
import DAO.SharedDBConfig.dbConfig.driver.api._
import controllers.AuthenticatedUsers.isUserAuthenticated
import models.{Communication, Communications, Message, Messages}
import models.User._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import scala.concurrent.{Await, Future}


class ChatController extends Controller {
  val messages = TableQuery[Messages]
val communications = TableQuery[Communications]

  private def validateRequest(request: Request[AnyContent], onAuthorized: Result, onUnauthorized: Result): Result =
    request.session.get("name") map { name =>
      if (isUserAuthenticated(name)) onAuthorized
      else onUnauthorized
    } getOrElse onUnauthorized


  def groupchat = Action { request =>
    validateRequest(request, Ok(views.html.groupChat()), Unauthorized( (views.html.auth())))
  }

  def getMsgs = Action.async {
    val result = dbConfig.db.run(messages.result)
    result.map(msgs => Ok(Json.obj("status" -> "Ok", "messages" -> Json.toJson(msgs))))
  }

  def createMsg = Action.async(BodyParsers.parse.json) { request =>
    val msg = request.body.validate[Message]
    msg.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)))),
      msg =>
        dbConfig.db.run( messages += msg).map(m =>
          Ok(Json.obj("status" -> "Success", "message" -> Json.toJson(m)))
        ))
  }

  def privatechat = Action { request =>
    validateRequest(request, Ok(views.html.privateChat()), Unauthorized( (views.html.auth())))
  }

  def getCommunications = Action.async {
    val result = dbConfig.db.run(communications.result)
    result.map(comms => Ok(Json.obj("status" -> "Ok", "communications" -> Json.toJson(comms))))
  }

  def createCommunication = Action.async(BodyParsers.parse.json) { request =>
    val comm = request.body.validate[Communication]
    comm.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)))),
      comm =>
        dbConfig.db.run( communications += comm).map(c =>
          Ok(Json.obj("status" -> "Success", "communication" -> Json.toJson(c)))
        ))
  }

}
