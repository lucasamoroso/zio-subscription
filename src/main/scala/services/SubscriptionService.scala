package com.lamoroso.example
package services

import zio.*
import zio.ZLayer

import java.sql.SQLException

import io.github.iltotore.iron.*

import com.lamoroso.example.config.AppConfig
import com.lamoroso.example.kafka.KafkaError.KafkaProducerError
import com.lamoroso.example.kafka.SubscriptionsProducer
import database.error.DatabaseError.SQLError
import database.repositories.SubscriptionRepository
import model.RefinedTypes.*
import model.Subscription
import model.api.{CreateSubscription, UpdateSubscription}
import services.error.ServiceError.SubscriptionNotFoundError

final case class SubscriptionService(
  config: AppConfig,
  repository: SubscriptionRepository,
  producer: SubscriptionsProducer
):
  def create(createSubscription: CreateSubscription): ZIO[Any, SQLError | KafkaProducerError, Subscription] =
    for {
      subscription <- Subscription.from(createSubscription)
      _            <- ZIO.logInfo(s"Creating subscription ${subscription.id}")
      _ <- repository
             .create(subscription)
             .logError(s"There was an error on attempt to create subscription ${subscription.id}")
             .mapError(_ => SQLError())
      _ <- producer.notify(subscription)
    } yield (subscription)

  def list(): ZIO[Any, SQLError, List[Subscription]] =
    ZIO.logInfo(s"Listing all subscriptions ") *>
      repository
        .list()
        .logError(s"There was an error on attempt to list subscriptions")
        .mapError(_ => SQLError())

  def get(subscriptionId: SubscriptionId): ZIO[Any, SQLError | SubscriptionNotFoundError, Subscription] =
    ZIO.logInfo(s"Looking for subscriptions ${subscriptionId}") *>
      repository
        .get(subscriptionId)
        .logError(s"There was an error on attempt to get subscription ${subscriptionId}")
        .mapError(_ => SQLError())
        .flatMap {
          case Some(value) => ZIO.succeed(value)
          case None        => ZIO.fail(SubscriptionNotFoundError(subscriptionId))
        }

  def delete(
    subscriptionId: SubscriptionId
  ): ZIO[Any, SQLError | SubscriptionNotFoundError | KafkaProducerError, Subscription] =
    for {
      _ <- ZIO.logInfo(s"Deleting subscription ${subscriptionId}")
      _ <- get(subscriptionId)
      subscription <- repository
                        .delete(subscriptionId)
                        .logError(s"There was an error on attempt to get subscription ${subscriptionId}")
                        .mapError(_ => SQLError())
      _ <- producer.notify(subscription)
    } yield subscription

  def update(
    subscriptionId: SubscriptionId,
    name: Name,
    email: Email
  ): ZIO[Any, SQLError | SubscriptionNotFoundError | KafkaProducerError, Subscription] =
    for {
      _ <- ZIO.logInfo(s"Updating subscription ${subscriptionId}")
      _ <- get(subscriptionId)
      subscription <-
        repository
          .update(subscriptionId, name, email)
          .logError(s"There was an error on attempt to update subscription ${subscriptionId}")
          .mapError(_ => SQLError())
      _ <- producer.notify(subscription)
    } yield subscription

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
