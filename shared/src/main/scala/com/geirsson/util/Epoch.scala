package com.geirsson.util

import java.util.Date

case class Epoch(millis: Long) {
  def getTime = millis
}

object Epoch {
  def now = new Date().getTime
}
