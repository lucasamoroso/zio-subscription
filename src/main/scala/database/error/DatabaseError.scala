package com.lamoroso.example
package database.error

import zio.json.*

sealed trait DatabaseError(message: String) extends Throwable

object DatabaseError:

  final case class SQLError(message: String = "There was an error processing the query") extends DatabaseError(message)

  implicit val codec: JsonCodec[SQLError] =
    DeriveJsonCodec.gen[SQLError]
