# Input, output, modes and screens

So in an ASCII roguelike, we have to worry a lot about what screen you are currently looking at, how to draw it, when to redraw it.
We also spend a lot of time waiting for input, looping round to redraw the screen, and processing it.

One of the big advantages of using a web system is that the Request/Response mechanism free's us from that looping, and we can use different paths in our web-app to render different screens.

In Trystans tutorial he creates 4 screens, a start screen, a play screen and two end screens.  Each progresses to the next via keyboard input, either the default input, or by allowing the choice via pressing enter or escape.

Our web-app is going to have 4 new paths you can request it on, /start, /play, /die and /win.  We are going to keep track of where the user should go next via a session variable, and we'll rewrite the / url to redirect to the appropriate handler.
Each path should render links or buttons that make it possible to go to the correct place.  We'll process these as a POST to teh current handler, with different options.

Let's get started with the /start path and the code to redirect to it from / in DispatcherServlet.

```
before("/*") {
	if (!session.contains("user")) {
		session("user") = User(ReadyToStart)
	}
}

get("/") {
	session("user") match {
		case ReadyToStart => redirect("/start")
		case x => halt(500, "Unknown state " + x)
	}
}
```

The states themselves are case objects that extend a parent trait, which is a reaosnable typesafe way of keeping the states stored nicely.

```
trait State
case object ReadyToStart extends State
case object InGame extends State
case object Lost extends State
case object Won extends State
```

Here we are setting up a rule that runs before every input, to set the intial session variable, then we've replaced the original code in get("/") with some new code to redirect the user to the appropriate url for their state.  There's a few things here we'll probably come back to as the code get's bigger, but lets carry on for now.  We still want that old code so we've just moved it to the get("/start") location now.

```
  get("/start") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    html.welcome.render(screen)
  }
```

Now we need to enable some actions.  I'm not really sure the best way to approach this, so lets start as simple as possible and see how long it lasts us.  We'll associate a set of links with each screen, render them, and the links will change the session variables and redirect the user to the right place.  We aren't worrying about security or even sanity at this point, just the most basic stuff.  Here's the first step, adding the action to the Dispatcher:

```
  get("/start") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    html.welcome.render(screen, Map("Start" -> "/action/start"))
  }
```

In the template we need to adjust the parameters at the top of the template
```
@(screen:Screen, actions: Map[String, String])
```

and render the list of actions in a useful way:

```
<ul>
	@actions.map { case (title,link) => 
		<li><a href="@link">@title</a></li>
	}
</ul>
```

Note here that matching on a map can give us an unapply for the Tuple2 object that can be broken down nicely.

Finally, back in the dispatcher we need to setip the new handler to handle the /action/start request.

```
  get("/action/start") {
    session("state") = InGame
    redirect("/")
  }
```

Here, I set the session state to the new state and redirect back to the root url.  This is inefficient, since that means that the user will then be prompty redirected to the next state page, so they'll get a double redirect, but it saves me keeping the logic of the next page in two places.  If this was an issue when we start playing, I'd think about making this skip the root url and going straight to the ingame url.

We also haven't got any checks for our action url, so the user can hit this action at anytime and set their state.  again we'll come back and address that later.

Adding the new controller and template is easy

```
  get("/game") {
    val screen = Screen(80, 24).write(1, 1, "You are playing")
    render(screen, Map("Win" -> "/action/win", "Lose" -> "/action/lose"))
  }

  get("/") {
    session("state") match {
		case ReadyToStart => redirect("/start")
		case InGame => redirect("/game")
		case x => halt(500, "Unknown state " + x)
	}
}

```

At this point I refactored out our render code into a local function, render, so that I wouldn't keep writing the same stuff.  I realised that I want to reuse my template, for now at least, since all screens will have the map, and a list of actions.  I just went ahead and implemented all of these the same, you cna checkout the code on github if you are interested.

That's us done for simple State and Screen storage.  We've got a very basic system.  There's no persistence, no validation or security, and a few code smells starting to creep up, but we've got a nice starting point for moving onto next week, creating a random map that's bigger than our viewport and scrolling through it.