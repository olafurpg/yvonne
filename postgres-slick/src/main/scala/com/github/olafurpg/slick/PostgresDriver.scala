package com.github.olafurpg.slick

import java.util.Date

import com.github.tminglei.slickpg._

case class Epoch(millis: Long) {
  def getTime = millis
}

object Epoch {
  def now = new Date().getTime
}

trait PostgresDriver extends ExPostgresDriver with PgArraySupport {
  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  override val api = MyAPI

  object MyAPI extends API
  with ArrayImplicits {

    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
  }
}

object PostgresDriver extends PostgresDriver
