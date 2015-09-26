package com.geirsson

import org.joda.time.DateTime

package object util {
  implicit def reonTime2DateTime(reonTime: Epoch): DateTime = new DateTime(reonTime.millis)
  implicit def long2EpochJvm(l: Long): Epoch = Epoch(l)
}
