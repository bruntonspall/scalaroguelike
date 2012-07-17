= Part 01: Scala, SBT, Scalatra, Appengine, Twirl, Bootstrap, HTML and CSS - Oh my

In trystans tutorial he was able to take some short cuts using the AsciiPanel library that he had created before.  It provided java applet and command line facilities to write to the screen and provided a nice interface.  The first build simply created a Panel that had a 24*80 window that rendered appropriately.

Here I have a decision to make.  I can build a system that outputs ASCII, and we can build a simple servlet that can output the correct HTML tags to lay that out, or we can use the Hypermedia portion of HTML, and use a more image/graphic interface.

Although it adds complexity, I'm keen on building a more visual experience for the web, so lets try that.

Our first challange is going to be working out what should be displayed.  If we stick with the 24*80 grid that Trystan used, we need to size our graphics appropriately.  I'm a rubbish artist, but even I can manage some very basic graphics that might do here.  Most browsers thesedays have screens that are 1024x768, but browser chrome uses up a varying amount of space.  If we build square graphics, say 10x10 pixels, we'll have 240x800 pixels used, which is going to look very flat.

In fact, to welcome you to the build environment, lets do that.

I used my giter8 project for building simple webapps on appengine in scala to build my initial system.  You'll need to ensure that you have installed appengine, SBT and giter8, and set an environment variable APPENGINE_SDK_HOME to the right place.  Because I use homebrew on my mac, this is a simple `brew install giter8 sbt app-engine-java-sdk` and then update my bashrc to set APPENGINE_SDK_HOME to /Users/mbrunton-spall/Developer/Cellar/app-engine-java-sdk/1.6.4.1/libexec

```
(mbrunton-spall@gnm31330 work/mbs-…like)% g8 bruntonspall/appengine-objectify-scala
...
(mbrunton-spall@gnm31330 work/mbs-…like)% sbt
...
scalaRogueLike > package
[info] Compiling twirl template .../welcome.scala.html to .../welcome.template.scala
[info] Formatting 5 Scala sources {file:/Users/mbrunton-spall/work/mbs-roguelike/}scalaRogueLike(compile) ...
[info] Reformatted 4 Scala sources {file:/Users/mbrunton-spall/work/mbs-roguelike/}scalaRogueLike(compile).
[info] Compiling 6 Scala sources to /Users/mbrunton-spall/work/mbs-roguelike/target/scala-2.9.1/classes...
[info] Packaging /Users/mbrunton-spall/work/mbs-roguelike/target/scala-2.9.1/scalaroguelike_2.9.1-1.0.war ...
[info] Done packaging.
[success] Total time: 13 s, completed Jul 17, 2012 8:58:29 AM
scalaRogueLike > appengine-dev-server
[info] Application not yet started
[info] Starting application in the background ...
[success] Total time: 0 s, completed Jul 17, 2012 8:59:03 AM
scalaRogueLike > 
...
Jul 17, 2012 8:59:17 AM com.google.appengine.tools.development.DevAppServerImpl start
INFO: The server is running at http://localhost:8080/
```

We now have a very simple scalatra appengine app up and running, which you can hit on http://localhost:8080/ and it should say welcome Testy Testerson.

Next we'll need some graphics, except while I'm playing with the graphic sizing, I don't want to have to keep creating a graphic, so we are going to use a broken link for now.  So open up welcome.scala.html, and add the following line below the h1 tag.
```
<img width=10 height=10 src="/x">
```

hit reload, and notice that nothing has changed.  Now flick to your SBT console and type package, notice that your welcome.scala.html will be compiled, and you should see a square on the page when you reload your browser now.
Unfortunately this isn't going to work right now, since we have no css-reset, and images have default borders and things.

Let's try again, we can style an empty element with HTML, so let's try creating a span with a background colour.  Now when they repeat I want to see how big they are, so we are going to create the block 1 pixel smaller, and give it a 1 pixel border.  We want to make sure that the blocks stack left to right, and that the browser doesn't do anything weird, so let's try this instead.

```
<span class="tile"></span>
<span class="tile"></span>
<span class="tile"></span>
```

and in the head, let's style these spans

```
<style type="text/css">
.tile {
	width: 9px;
	height: 9px;
	margin: 0;
	border: 0;
	border-right: solid 1px #000;
	border-bottom: solid 1px #000;
	padding: 0;
	background-color: #999;
	display: block;
	float: left;
}
</style>
```

Save it, reload and oh, we need to package again.  SBT has a nice feature, where if instead of typing package you type ~package it will watch for on-disk changes that would require re-running the target command.  If you type that, you should get on every save an attempt to package the webapp again.  Unfortunately due to Appengine and SBT combined, there's abouta  5 second delay between saving and being able to reload the HTML, but it's better than retyping package each time.

This works, I get a couple of grey boxes with a black border.  So let's make an 24x80 grid of them.  So far we've only been modifying the HTML, and we could do this again, but I want to get to modelling a Screen object in the code, so let's give that a try.  In the src/main/scala/package/model directory create a file called Screen.scala and give it the contents

```
package uk.co.bruntonspall.roguelike.model

case class Screen(width: Int, height: Int) {
  def rows = 1 to height
  def columns = 1 to width
}
```
This defines a screen class that can have a height and width, and we can get a range of the x and y coordinates, so Screen(4,4).rows returns [1, 2, 3, 4].
We can update the servlet to construct a new screen object on each request.
```
get("/") {
    val screen = Screen(80, 24)
    html.welcome.render(screen)
}
```
We can now update the html to have a row div, and produce the correct screen information, like so:
```
.row {
	clear: both;
}
```
and
```
<h1>Welcome</h1>
@screen.rows.map { y =>
	<div class="row">
	@screen.columns.map { x =>
		<span class="tile"></span>
	}
	</div>
}
```

When we reload the page now, we'll have a lovely array of squares, 80 wide and 24 high.  But they seem a little small and weirdly shaped.

This is because classic terminals are 4x3 ratio screens, and therefore normal text characters are the same ratio.  Most actual character sets use a 16x12 character set.  If we try setting it to 16x12 (actually 15 by 11), we can see that it's beginning to look a bit more like a standard grid.

Finally we need to be able to display text on the grid.  We might come back later to add some other more HTML/Web like facilities to our display, but for now, we are going to treat this box as a 24x80 character display.  Let's add a write function to our screen object and see what happens.

```
  def charAt(x: Int, y: Int): Char = buffer.getOrElse((x, y), ' ')
  def withCharAt(x: Int, y: Int, char: Char) = Screen(width, height, buffer.updated((x, y), char))
  def write(x: Int, y: Int, text: List[Char]): Screen = text match {
    case head :: Nil => withCharAt(x, y, head)
    case head :: tail => write(x + 1, y, tail).withCharAt(x, y, head)
    case Nil => this
  }
  def write(x: Int, y: Int, text: String): Screen = write(x, y, text.toList)
```

This is probably a bit wasteful, but we are keeping the scala style of being immutable here, so our screen itself is not changing, but the ability to write a character to the screen is done by returning a new screen with the character in place.  We'll come back later and check the performance impact of this if we do a lot of writing and re-writing, but it will do for now.

We add the necessary code to the DispatcherServlet:

```
get("/") {
    val screen = Screen(80, 24).write(1, 1, "Welcome to Roguelike")
    html.welcome.render(screen)
}	
```

and we update the HTML to render our font at the right size, and in keeping with a roguelike, in fixed-width style terminal font.

```
.tile {
    ...
	text-align: center;
	font-family: Courier, fixed;
	font-size: 13px;
}

```

And we are done, we have something similar to the original version.  We've had to do a bit more work because we didn't have the handy AsciiPanel library that was pre-created.  We are missing a few features that the AsciiPanel library supports, terminal colours at a glace, but I think we can do something about that once we get there.