package models

import java.sql.{Date}
import slick.driver.SQLiteDriver.api._
import slick.lifted.Tag


  class Tasks (tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column [Int]("id", O.PrimaryKey)
    def number = column[String]("number")
    def description = column[String]("description")
    def category = column[String]("category")
    def date = column[Date]("date")

    def * = (id, number, description, category, date) <> ((Task.apply _).tupled, Task.unapply _)
  }


