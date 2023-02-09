package com.lamoroso.example
package model.api

import zio.json.DeriveJsonCodec
import zio.json.JsonCodec

final case class CreateSubscription(name: String, email: String)

object CreateSubscription:
  /**
   * Derives a JSON codec for the CreateSubscription type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[CreateSubscription] =
    DeriveJsonCodec.gen[CreateSubscription]
