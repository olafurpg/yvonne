package com.geirsson

import java.util.Date

import org.joda.time.DateTime

package object util {
  implicit def epoch2DateTime(reonTime: Epoch): DateTime = new DateTime(reonTime.millis)
  implicit def epoch2JavaDate(reonTime: Epoch): Date = new Date(reonTime.millis)
  implicit def date2Epoch(date: Date): Epoch = Epoch(date.getTime)
  implicit def long2EpochJvm(l: Long): Epoch = Epoch(l)
}
