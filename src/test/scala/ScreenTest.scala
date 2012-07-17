import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import uk.co.bruntonspall.roguelike.model.Screen

class ScreenTest extends FlatSpec with ShouldMatchers {
  "A Screen" should "be initialised" in {
    val s = Screen(4, 4)
    s.rows should equal(1 to 4)
    s.columns should equal(1 to 4)
    for (y <- s.columns) {
      for (x <- s.rows) {
        s.charAt(x, y) should equal(' ')
      }
    }
  }

  it should "be written to with one character" in {
    val before = Screen(6, 6)
    val after = before.withCharAt(1, 1, 'x')
    before.charAt(1, 1) should equal(' ')
    after.charAt(1, 1) should equal('x')
  }

  it should "be written to with a string" in {
    val before = Screen(6, 6)
    val after = before.write(1, 1, "abc")
    before.charAt(1, 1) should equal(' ')
    before.charAt(2, 1) should equal(' ')
    before.charAt(3, 1) should equal(' ')
    before.charAt(4, 1) should equal(' ')
    after.charAt(1, 1) should equal('a')
    after.charAt(2, 1) should equal('b')
    after.charAt(3, 1) should equal('c')
    after.charAt(4, 1) should equal(' ')

  }
}