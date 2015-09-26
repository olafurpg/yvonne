package com.geirsson

package object util {
  implicit def long2Epoch(l: Long): Epoch = Epoch(l)
}
