package com.lamoroso.example
package model.error

import zio.json.DeriveJsonCodec
import zio.json.JsonCodec

sealed trait SerDeError(message: String) extends Throwable

object SerDeError:
  final case class EmptyBodyError(message: String = "The body provided was empty") extends SerDeError(message)
  final case class InvalidJsonBodyError(message: String = "The body provided is not a valid")
      extends SerDeError(message)

  /**
   * Derives a JSON codec for the UserError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[SerDeError] =
    DeriveJsonCodec.gen[SerDeError]
