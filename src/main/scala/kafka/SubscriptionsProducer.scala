package com.lamoroso.example
package kafka

import zio.*
import zio.durationInt
import zio.kafka.producer.Producer
import zio.kafka.producer.ProducerSettings
import zio.kafka.serde.Serde

import scala.util.control.NonFatal

import com.lamoroso.example.config.AppConfig
import com.lamoroso.example.kafka.KafkaError.KafkaProducerError
import com.lamoroso.example.model.Subscription
import org.apache.kafka.clients.producer.RecordMetadata

final case class SubscriptionsProducer(config: AppConfig, producer: Producer):
  def notify(subscription: Subscription): ZIO[Any, KafkaProducerError, RecordMetadata] =
    ZIO.logInfo(s"Notifying change in subscription ${subscription.id}") *>
      producer
        .produceAsync[Any, String, Subscription](
          topic = config.kafka.subscriptionTopic,
          key = subscription.id.toString(),
          value = subscription,
          keySerializer = Serde.string,
          valueSerializer = SubscriptionSerializer.serializer
        )
        .flatMap(task =>
          task.logError(s"There was an error producing message from subscription ${subscription.id}. Retrying...") *>
            // If we don't receive an acknowledgement from the broker for the transmission of the record we'll retry it, we should be more careful about the errors being refined because maybe there are only a couple of them that we want retry
            task.unrefine { case NonFatal(_) =>
              notify(subscription)
            }
        )
        .logError(s"There was an error producing message from subscription ${subscription.id}. Retrying...")
        //If there was an error enqueueing  the record to the Producer's internal buffer retry up to 5 times
        .retry(Schedule.exponential(10.millis) && Schedule.recurs(5))
        .mapError(_ => KafkaProducerError(s"There was an error processing subscription ${subscription.id}"))

object SubscriptionsProducer:
  val layer = ZLayer.scoped {
    for {
      config   <- ZIO.service[AppConfig]
      producer <- Producer.make(settings = ProducerSettings(config.kafka.brokers))
    } yield SubscriptionsProducer(config, producer)
  }
