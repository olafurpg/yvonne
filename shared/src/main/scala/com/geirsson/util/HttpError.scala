package com.geirsson.util

case class HttpError(status: Int) extends Exception
object NotFound extends HttpError(404)
object Unauthorized extends HttpError(401)
