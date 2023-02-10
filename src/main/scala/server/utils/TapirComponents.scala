package com.lamoroso.example
package server.utils

import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec
import sttp.tapir.CodecFormat.TextPlain

import scala.util.*

import java.util.UUID

import com.lamoroso.example.model.RefinedTypes.*
import com.lamoroso.example.model.Subscription
import com.lamoroso.example.model.SubscriptionId
import com.lamoroso.example.model.error.ServiceError
import com.lamoroso.example.model.error.ServiceError.DatabaseError
import com.lamoroso.example.model.error.ServiceError.SubscriptionNotFoundError
import io.github.iltotore.iron.*

object TapirComponents:

  def decodeSubscriptionId(uuid: String): DecodeResult[SubscriptionId] = SubscriptionId.from(uuid) match {
    case Success(v) => DecodeResult.Value(v)
    case Failure(f) => DecodeResult.Error(uuid, f)
  }

  def encodeSubscriptionId(subscriptionId: SubscriptionId): String = subscriptionId.value.toString()

  implicit val subscriptionIdCodec: Codec[String, SubscriptionId, TextPlain] =
    Codec.string.mapDecode(decodeSubscriptionId)(encodeSubscriptionId)

  implicit lazy val subscriptionIdSchema: Schema[SubscriptionId] = Schema.string

  implicit lazy val nameSchema: Schema[Name] = Schema.string

  implicit lazy val emailSchema: Schema[Email] = Schema.string
