package services

import scala.concurrent.Future

trait MyApi{

  def doThing(i: Int, j: Int): Int

  def gimmeStrings(i: Int, str: String): Seq[String]
}

trait MySecondApi{

  def doThing2(i: Int, j: Int): Future[Int]

  def gimmeStrings2(i: Int, str: String): Seq[String]
}
