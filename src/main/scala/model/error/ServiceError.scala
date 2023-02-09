package com.lamoroso.example
package model.error

import zio.json.*

import sttp.tapir.Schema

sealed trait ServiceError(message: String) extends Throwable

object ServiceError:
  final case class DatabaseError(message: String = "There was an error processing the query")
      extends ServiceError(message)
  object DatabaseError:
    implicit val codec: JsonCodec[DatabaseError] =
      DeriveJsonCodec.gen[DatabaseError]

    /**
     * Endpoint documentation with tapir
     */
    implicit val schema: Schema[DatabaseError] = Schema.derived[DatabaseError]

  /**
   * Derives a JSON codec for the SerDeError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[ServiceError] =
    DeriveJsonCodec.gen[ServiceError]

  /**
   * Endpoint documentation with tapir
   */
  implicit val schema: Schema[ServiceError] = Schema.derived[ServiceError]
