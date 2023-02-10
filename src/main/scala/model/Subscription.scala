package com.lamoroso.example
package model

import zio.UIO

import zio.json.*

import scala.util.Failure
import scala.util.Success

import api.CreateSubscription
import io.github.iltotore.iron.zioJson.given
import model.RefinedTypes.*

case class Subscription(id: SubscriptionId, name: Name, email: String)

object Subscription:
  def from(createSubscription: CreateSubscription): UIO[Subscription] =
    SubscriptionId.random.map(id => Subscription(id, createSubscription.name, createSubscription.email))

  /**
   * Derives a JSON codec for the Subscription type allowing it to be
   * (de)serialized.
   */
  implicit lazy val codec: JsonCodec[Subscription] =
    DeriveJsonCodec.gen[Subscription]
