package com.geirsson.util

case class HttpError(status: Int) extends Exception
object NotFound extends HttpError(404)
object UnauthorizedError extends HttpError(401)
