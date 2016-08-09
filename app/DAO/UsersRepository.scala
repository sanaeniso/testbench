package DAO

import SharedDBConfig.dbConfig.driver.api._
import models.{Tasks, Task, User, Users}

object UsersRepository {

  val users = TableQuery[Users]
  val tasks = TableQuery[Tasks]

  def allUsers = users.result

  def findByLogin(login: String) = users.filter( _.login === login ).result

  def findQuery(id: Int) = users.filter(_.id === id).result

  def findByName(name: String) = users.filter(_.name === name).result

  def exist(name: String, password: String) = users.filter(u => u.name === name && u.password === password).exists.result

  def updateUser(user: User) = users.filter(_.id === user.id).update(user)

  def deleteUser(name: String) = users.filter(_.name === name).delete


  def deleteTask(number: String) = tasks.filter(_.number === number).delete
}
