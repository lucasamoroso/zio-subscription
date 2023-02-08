package com.lamoroso.example
package model

import zio.json.*

case class Subscription(name: String, mail: String, age: Int)

object Subscription:
  /**
   * Derives a JSON codec for the Subscription type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[Subscription] =
    DeriveJsonCodec.gen[Subscription]
