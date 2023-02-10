package com.lamoroso.example
package model.api

import zio.json.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.zioJson.given
import model.RefinedTypes.*

final case class CreateSubscription(name: Name, email: String)

object CreateSubscription:
  /**
   * Derives a JSON codec for the CreateSubscription type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[CreateSubscription] =
    DeriveJsonCodec.gen[CreateSubscription]
