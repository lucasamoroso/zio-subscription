package com.lamoroso.example
package model

import zio._

import zio.json._

import scala.util.Try

import java.util.UUID

final case class SubscriptionId(value: UUID) {
  override def toString(): String = value.toString()
}

object SubscriptionId:

  /** Generates a Random UUID and wraps it in the SubscriptionId type */
  def random: UIO[SubscriptionId] = Random.nextUUID.map(SubscriptionId(_))

  implicit lazy val codec: JsonCodec[SubscriptionId] =
    JsonCodec[UUID].transform(SubscriptionId(_), _.value)
