package com.lamoroso.example
package model.error

import zio.json.DeriveJsonCodec
import zio.json.JsonCodec

sealed trait SerDeError(message: String) extends Throwable

object SerDeError:
  final case class EmptyBodyError(message: String = "The body provided was empty") extends SerDeError(message)
  object EmptyBodyError:
    implicit val codec: JsonCodec[EmptyBodyError] =
      DeriveJsonCodec.gen[EmptyBodyError]
  final case class InvalidJsonBodyError(message: String = "The body provided is not a valid")
      extends SerDeError(message)
  object InvalidJsonBodyError:
    implicit val codec: JsonCodec[InvalidJsonBodyError] =
      DeriveJsonCodec.gen[InvalidJsonBodyError]

  /**
   * Derives a JSON codec for the SerDeError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[SerDeError] =
    DeriveJsonCodec.gen[SerDeError]
