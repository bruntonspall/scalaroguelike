package uk.co.bruntonspall.roguelike.model

case class Screen(width: Int, height: Int, buffer: Map[(Int, Int), Char] = Map.empty) {
  def rows = 1 to height
  def columns = 1 to width
  def charAt(x: Int, y: Int): Char = buffer.getOrElse((x, y), ' ')
  def withCharAt(x: Int, y: Int, char: Char) = Screen(width, height, buffer.updated((x, y), char))
  def write(x: Int, y: Int, text: List[Char]): Screen = text match {
    case head :: Nil => withCharAt(x, y, head)
    case head :: tail => write(x + 1, y, tail).withCharAt(x, y, head)
    case Nil => this
  }
  def write(x: Int, y: Int, text: String): Screen = write(x, y, text.toList)
}