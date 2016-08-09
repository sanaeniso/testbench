package models
import slick.driver.SQLiteDriver.api._
import slick.lifted.Tag

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def login = column[String]("Login")
  def name = column[String]("NAME")
  def email = column[String]("EMAIL")
  def password = column[String]("PASSWORD")

  def * = (id, login, name, email, password) <> ((User.apply _).tupled, User.unapply)
}

