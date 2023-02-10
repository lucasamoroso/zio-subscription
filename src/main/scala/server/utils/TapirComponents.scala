package com.lamoroso.example
package server.utils

import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec

import scala.util.*

import com.lamoroso.example.model.Subscription
import com.lamoroso.example.model.SubscriptionId
import com.lamoroso.example.model.error.ServiceError
import com.lamoroso.example.model.error.ServiceError.DatabaseError
import com.lamoroso.example.model.error.ServiceError.SubscriptionNotFoundError

object TapirComponents:

  def decode(subscriptionId: String): DecodeResult[SubscriptionId] =
    SubscriptionId.from(subscriptionId) match {
      case Success(v) => DecodeResult.Value(v)
      case Failure(f) => DecodeResult.Error(subscriptionId, f)
    }

  def encode(subscriptionId: SubscriptionId): String = subscriptionId.id.toString()

  implicit val subscriptionIdCodec: PlainCodec[SubscriptionId] = Codec.string.mapDecode(decode)(encode)
