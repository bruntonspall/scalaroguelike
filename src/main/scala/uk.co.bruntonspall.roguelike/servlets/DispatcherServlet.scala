package uk.co.bruntonspall.roguelike.servlets

import org.scalatra.ScalatraServlet
import uk.co.bruntonspall.roguelike.scalatra.TwirlSupport
import uk.co.bruntonspall.roguelike.model.User
import uk.co.bruntonspall.roguelike.util.Ofy

class DispatcherServlet extends ScalatraServlet with TwirlSupport {

  get("/") {
    val userToSave = new User("test@test.com", "password", "Testy Testerson")
    Ofy.save.entity(userToSave).now()
    val user = Ofy.load.`type`(classOf[User]).id("test@test.com").get
    html.welcome.render(user)
  }

}