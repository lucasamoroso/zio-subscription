package com.lamoroso.example
package model.api

import zio.json.*

import model.SubscriptionId

final case class UpdateSubscription(id: SubscriptionId, name: String, email: String)

object UpdateSubscription:
  implicit lazy val codec: JsonCodec[UpdateSubscription] =
    DeriveJsonCodec.gen[UpdateSubscription]
