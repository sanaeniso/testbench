package controllers

object AuthenticatedUsers {

  private var connectedUsers: List[String] = List()

  def isUserAuthenticated(name: String): Boolean =
    connectedUsers contains name

  def registerUser(name: String): Unit =
    connectedUsers = connectedUsers ++ List(name)

  def unregisterUser(name: String): Unit =
    connectedUsers = connectedUsers.filterNot(_ == name)
}
