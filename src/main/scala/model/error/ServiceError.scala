package com.lamoroso.example
package model.error

import zio.json.*

sealed trait ServiceError(message: String) extends Throwable

object ServiceError:
  final case class DatabaseError(message: String = "There was an error processing the query")
      extends ServiceError(message)

  /**
   * Derives a JSON codec for the SerDeError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[ServiceError] =
    DeriveJsonCodec.gen[ServiceError]
