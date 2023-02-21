package com.lamoroso.example
package model.error

import zio.json.*

import io.github.iltotore.iron.zioJson.given

import model.RefinedTypes.*

sealed trait ServiceError(message: String) extends Throwable

object ServiceError:

  final case class SubscriptionNotFoundError(
    id: SubscriptionId,
    message: String = "The subscription was not found in our systems"
  ) extends ServiceError(message)

  object SubscriptionNotFoundError:
    implicit val codec: JsonCodec[SubscriptionNotFoundError] =
      DeriveJsonCodec.gen[SubscriptionNotFoundError]

  /**
   * Derives a JSON codec for the SerDeError type allowing it to be
   * (de)serialized.
   */
  implicit val codec: JsonCodec[ServiceError] =
    DeriveJsonCodec.gen[ServiceError]
