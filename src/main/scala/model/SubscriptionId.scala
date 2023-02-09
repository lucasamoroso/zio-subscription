package com.lamoroso.example
package model

import zio._

import zio.json._

import sttp.tapir.Schema

import java.util.UUID

final case class SubscriptionId(id: UUID)

object SubscriptionId:

  /** Generates a Random UUID and wraps it in the SubscriptionId type */
  def random: UIO[SubscriptionId] = Random.nextUUID.map(SubscriptionId(_))

  implicit val codec: JsonCodec[SubscriptionId] =
    JsonCodec[UUID].transform(SubscriptionId(_), _.id)

  /**
   * Endpoint documentation with tapir
   */
  implicit val schema: Schema[SubscriptionId] = Schema.derived[SubscriptionId]
