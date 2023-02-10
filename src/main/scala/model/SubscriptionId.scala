package com.lamoroso.example
package model

import zio._

import zio.json.*
import zio.json._

import scala.util.Try

import java.util.UUID

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

final case class SubscriptionId(value: String :| ValidUUID) {
  override def toString(): String = value.toString()
}

object SubscriptionId:
  def from(uuid: String): SubscriptionId = SubscriptionId(uuid.refine)
  def from(uuid: UUID): SubscriptionId   = SubscriptionId.from(uuid.toString())

  /** Generates a Random UUID and wraps it in the SubscriptionId type */
  def random =
    Random.nextUUID.map(uuid => SubscriptionId.from(uuid))

  implicit lazy val codec: JsonCodec[SubscriptionId] =
    JsonCodec[String].transform(uuid => SubscriptionId.from(uuid), _.value)
