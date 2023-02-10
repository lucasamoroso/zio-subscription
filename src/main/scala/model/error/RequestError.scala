package com.lamoroso.example
package model.error

import zio.json.*

sealed trait RequestError(message: String) extends Throwable

object RequestError:
  final case class ParamMismatchError(
    param: String,
    message: String = "The param provided doesn't match with the one provided in the request body"
  ) extends RequestError(message)

  object ParamMismatchError:
    implicit val codec: JsonCodec[ParamMismatchError] =
      DeriveJsonCodec.gen[ParamMismatchError]
