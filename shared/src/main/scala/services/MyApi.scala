package services

trait MyApi{
  def doThing(i: Int, j: Int): Int
  def gimmeStrings(i: Int, str: String): Seq[String]
}
