package uk.co.bruntonspall.roguelike.servlets

import org.scalatra.ScalatraServlet
import org.scalatra.ApiFormats
import uk.co.bruntonspall.roguelike.scalatra.TwirlSupport
import uk.co.bruntonspall.roguelike.model._

class DispatcherServlet extends ScalatraServlet with TwirlSupport with ApiFormats {

  def render(screen: Screen, actions: Map[String, String]) =
    format match {
      case "html" => html.welcome.render(screen, actions)
      case _ => screen.rows.map { y => screen.columns.map { x => screen.charAt(x, y) }.mkString }.mkString("\n")
    }

  before("/*") {
    if (!session.contains("state")) {
      session("state") = ReadyToStart
    }
  }

  get("/action/start") {
    session("state") = InGame
    redirect("/")
  }

  get("/action/win") {
    session("state") = Won
    redirect("/")
  }

  get("/action/lose") {
    session("state") = Lost
    redirect("/")
  }

  get("/action/restart") {
    session("state") = ReadyToStart
    redirect("/")
  }

  get("/start") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    render(screen, Map("Start" -> "/action/start"))
  }

  get("/game") {
    val screen = Screen(80, 24).write(1, 1, "You are playing")
    render(screen, Map("Win" -> "/action/win", "Lose" -> "/action/lose"))
  }

  get("/won") {
    val screen = Screen(80, 24).write(1, 1, "You have won, play again?")
    render(screen, Map("Play again" -> "/action/restart"))
  }

  get("/lost") {
    val screen = Screen(80, 24).write(1, 1, "You have lost, play again?")
    render(screen, Map("Play again" -> "/action/restart"))
  }

  get("/") {
    session("state") match {
      case ReadyToStart => redirect("/start")
      case InGame => redirect("/game")
      case Lost => redirect("/won")
      case Won => redirect("/lost")
      case x => halt(500, "Unknown state " + x)
    }
  }

}