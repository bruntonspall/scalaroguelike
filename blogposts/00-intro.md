= MBS's Roguelike tutorial in scala

So apparently this is a thing now, people read [[Trystans intersting roguelike tutorial][http://trystans.blogspot.com/2011/08/nameless-roguelike-for-tutorial.html]] and build it in their favourite language and style.  Always up for a challange, I decided to do the same thing. 

Firstly, I was struck by some of the decisions that Trystan took that I wouldn't. A lot of Good Programming(tm) techniques that I just wouldn't use.  Part of that might be driven by Java, so I thought a scala version would be more interesting.

I've also several times tried to write a roguelike that can be run on a website.  Since a roguelike as a game is fully tunr based, nothing happens except in response to use input, it feels like a good match for a request/response framework.

My biggest problems will be:
 * Displaying the grid.  Roguelikes are ASCII generally, but it feels a bit rubbish to build a fully ascii webpage.
 * Building the action list.  A good roguelike can have hundreds of commands.
 * Sessions / saving the game state. We'll come back to this, for the beginning of the tutorial, we'll assume you are running it locally, and are the only user.  I'm not going to serialise the gamestate to a database yet.

 I've given myself a rundown based on trystans original list, but updated with some web development notes.  Here's what you should be expecting:

  * Part 01: Scala, SBT, Scalatra, Appengine, Twirl, Bootstrap, HTML and CSS - Oh my
  * Part 02: Input and output, modes and screens
  * Part 03: Scrolling through random caves
  * Part 04: The Player
  * part 05: Stationary monsters
  * part 06: hitpoints, combat, messages
  * part 07: z-levels and deeper caves
  * part 08: vision, line of sighty and fields of view
  * part 09: wandering monsters
  * part 10: items, inventory and inventory screens
  * part 11: hunger and food
  * part 12: weapons and armour
  * part 13: aggressive monsters
  * part 14: experience and levelling up
  * part 15: help, examine and look screens
  * part 16: throwing and ranged weapons
  * part 17: smarter monsters
  * part 18: potions and effects
  * part 19: mana, spells and magic books
  * part 20: item appearance and identification

  These are scheduled to be launched, one per week for the next 20 weeks, on Friday mornings, so stick with us.

  The code for this can be found at http://github.com/bruntonspall/scalaroguelike and you can play it at roguelike.brunton-spall.co.uk

  So hold on to your hats, here we go.

  