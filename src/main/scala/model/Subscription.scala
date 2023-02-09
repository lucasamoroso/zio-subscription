package com.lamoroso.example
package model

import zio.UIO

import zio.json.*

import sttp.tapir.Schema

import api.CreateSubscription

case class Subscription(id: SubscriptionId, name: String, email: String)

object Subscription:
  def from(createSubscription: CreateSubscription): UIO[Subscription] =
    SubscriptionId.random.map(id => Subscription(id, createSubscription.name, createSubscription.email))

  /**
   * Derives a JSON codec for the Subscription type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[Subscription] =
    DeriveJsonCodec.gen[Subscription]

  /**
   * Endpoint documentation with tapir
   */
  implicit val schema: Schema[Subscription] = Schema.derived[Subscription]
