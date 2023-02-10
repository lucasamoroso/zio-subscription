package com.lamoroso.example
package model

import zio.Random
import zio.UIO

import zio.json.*

import scala.util.Failure
import scala.util.Success

import io.github.iltotore.iron.zioJson.given

import api.CreateSubscription
import model.RefinedTypes.*

case class Subscription(id: SubscriptionId, name: Name, email: Email)

object Subscription:
  def from(createSubscription: CreateSubscription): UIO[Subscription] =
    Random.nextUUID.map(uuid => Subscription(uuid.asSubscriptionId, createSubscription.name, createSubscription.email))

  /**
   * Derives a JSON codec for the Subscription type allowing it to be
   * (de)serialized.
   */
  implicit lazy val codec: JsonCodec[Subscription] =
    DeriveJsonCodec.gen[Subscription]
