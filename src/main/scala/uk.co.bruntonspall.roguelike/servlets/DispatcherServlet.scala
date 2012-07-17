package uk.co.bruntonspall.roguelike.servlets

import org.scalatra.ScalatraServlet
import uk.co.bruntonspall.roguelike.scalatra.TwirlSupport
import uk.co.bruntonspall.roguelike.model.Screen
import uk.co.bruntonspall.roguelike.util.Ofy

class DispatcherServlet extends ScalatraServlet with TwirlSupport {

  get("/") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    html.welcome.render(screen)
  }

}