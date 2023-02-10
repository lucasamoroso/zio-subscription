package com.lamoroso.example
package model.api

import zio.json.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.zioJson.given

import model.RefinedTypes.*

final case class UpdateSubscription(id: SubscriptionId, name: Name, email: Email)

object UpdateSubscription:
  implicit lazy val codec: JsonCodec[UpdateSubscription] =
    DeriveJsonCodec.gen[UpdateSubscription]
