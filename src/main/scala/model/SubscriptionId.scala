package com.lamoroso.example
package model

import zio._

import zio.json._

import scala.util.Try

import java.util.UUID

final case class SubscriptionId(id: UUID)

object SubscriptionId:

  /** Generates a Random UUID and wraps it in the SubscriptionId type */
  def random: UIO[SubscriptionId] = Random.nextUUID.map(SubscriptionId(_))

  def from(uuid: String) = Try(SubscriptionId(UUID.fromString(uuid)))

  implicit lazy val codec: JsonCodec[SubscriptionId] =
    JsonCodec[UUID].transform(SubscriptionId(_), _.id)
