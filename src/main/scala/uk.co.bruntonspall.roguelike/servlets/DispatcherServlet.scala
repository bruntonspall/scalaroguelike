package uk.co.bruntonspall.roguelike.servlets

import org.scalatra.ScalatraServlet
import org.scalatra.ApiFormats
import uk.co.bruntonspall.roguelike.scalatra.TwirlSupport
import uk.co.bruntonspall.roguelike.model.Screen
import uk.co.bruntonspall.roguelike.util.Ofy

class DispatcherServlet extends ScalatraServlet with TwirlSupport with ApiFormats {

  get("/") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    format match {
      case "html" => html.welcome.render(screen)
      case _ => screen.rows.map { y => screen.columns.map { x => screen.charAt(x, y) }.mkString }.mkString("\n")
    }
  }

}