package com.lamoroso.example
package server

import zio.*

import zio.json.*

import zhttp.http.Request

import model.api.error.UserError
import model.api.error.UserError.*

/**
 * ServerUtils houses helper functions for parsing various elements of the API
 * including request bodies and specific IDs.
 *
 * This allows us to explicitly define what we are parsing and to provide custom
 * error messages.
 */
object ServerUtils:

  /**
   * Parses a request using a JSON decoder type class for the A type to decode
   * an A from the request's string body. In the case of a failure we are
   * returning a custom AppError type
   */
  def parseBody[A: JsonDecoder](request: Request): IO[UserError, A] =
    for {
      body <- request.body.asString
                .logError(s"The body ${request.body} provided by the user was empty")
                .mapError(_ => EmptyBodyError())
      parsed <- ZIO
                  .from(body.fromJson[A])
                  .logError(s"The body $body provided by the user is not deserializable")
                  .mapError(_ => InvalidJsonBodyError())
    } yield parsed
