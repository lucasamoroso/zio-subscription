package com.lamoroso.example
package server.utils

import sttp.tapir.*
import sttp.tapir.Codec.PlainCodec
import sttp.tapir.CodecFormat.TextPlain

import scala.util.*

import java.util.UUID

import io.github.iltotore.iron.*

import com.lamoroso.example.model.RefinedTypes.*

object TapirComponents:

  implicit lazy val subscriptionIdSchema: Schema[SubscriptionId] = Schema.string

  implicit lazy val nameSchema: Schema[Name] = Schema.string

  implicit lazy val emailSchema: Schema[Email] = Schema.string
