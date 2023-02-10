package com.lamoroso.example
package server.utils

import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec

import scala.util.*

import java.util.UUID

import com.lamoroso.example.model.Subscription
import com.lamoroso.example.model.SubscriptionId
import com.lamoroso.example.model.error.ServiceError
import com.lamoroso.example.model.error.ServiceError.DatabaseError
import com.lamoroso.example.model.error.ServiceError.SubscriptionNotFoundError

object TapirComponents:

  implicit lazy val subscriptionIdCodec: Codec[String, SubscriptionId, CodecFormat.TextPlain] =
    Codec.string.map[SubscriptionId](uuid => SubscriptionId.from(uuid))(subscriptionId =>
      subscriptionId.value.toString()
    )

  implicit lazy val subscriptionIdSchema: Schema[SubscriptionId] = Schema.string
