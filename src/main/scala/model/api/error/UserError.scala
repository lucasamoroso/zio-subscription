package com.lamoroso.example
package model.api.error

import zio.json.DeriveJsonCodec
import zio.json.JsonCodec

sealed trait UserError(message: String) extends Throwable

object UserError:
  final case class EmptyBodyError(message: String = "The body provided was empty")            extends UserError(message)
  final case class InvalidJsonBodyError(message: String = "The body provided is not a valid") extends UserError(message)

  /**
   * Derives a JSON codec for the UserError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[UserError] =
    DeriveJsonCodec.gen[UserError]
