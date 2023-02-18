package com.lamoroso.example
package kafka

import zio.ZIO
import zio.ZLayer
import zio.kafka.producer.Producer
import zio.kafka.producer.ProducerSettings
import zio.kafka.serde.Serde

import com.lamoroso.example.config.AppConfig
import com.lamoroso.example.model.Subscription
import model.error.ServiceError.DatabaseError

final case class SubscriptionsProducer(config: AppConfig, producer: Producer):
  def notify(subscription: Subscription) =
    ZIO.logInfo(s"Notifying change in subscription ${subscription.id}") *>
      producer
        .produce[Any, String, Subscription](
          topic = config.kafka.subscriptionTopic,
          key = subscription.id.toString(),
          value = subscription,
          keySerializer = Serde.string,
          valueSerializer = SubscriptionSerializer.serializer
        )
        .mapError(_ => DatabaseError()) // TODO: Add new Error type

object SubscriptionsProducer:
  val layer = ZLayer.scoped {
    for {
      config   <- ZIO.service[AppConfig]
      producer <- Producer.make(settings = ProducerSettings(config.kafka.brokers))
    } yield SubscriptionsProducer(config, producer)
  }
