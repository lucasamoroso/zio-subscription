package com.lamoroso.example
package model.api

import zio.json.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.zioJson.given
import model.RefinedTypes.*
import model.SubscriptionId

final case class UpdateSubscription(id: SubscriptionId, name: String :| Name, email: String)

object UpdateSubscription:
  implicit lazy val codec: JsonCodec[UpdateSubscription] =
    DeriveJsonCodec.gen[UpdateSubscription]
