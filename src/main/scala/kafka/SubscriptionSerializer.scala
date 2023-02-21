package com.lamoroso.example
package kafka

import zio.*
import zio.kafka.serde.Serde
import zio.kafka.serde.Serializer

import zio.json.*

import com.lamoroso.example.model.Subscription
import org.apache.kafka.common.header.Headers

object SubscriptionSerializer:
  lazy val serializer = new Serializer[Any, Subscription] {
    def serialize(topic: String, headers: Headers, msg: Subscription): RIO[Any, Array[Byte]] =
      ZIO.attempt(msg.toJson.getBytes)
  }
