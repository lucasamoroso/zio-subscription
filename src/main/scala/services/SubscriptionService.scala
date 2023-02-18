package com.lamoroso.example
package services

import zio.*
import zio.ZLayer

import java.sql.SQLException

import io.github.iltotore.iron.*

import com.lamoroso.example.config.AppConfig
import com.lamoroso.example.kafka.SubscriptionsProducer
import database.repositories.SubscriptionRepository
import model.RefinedTypes.*
import model.Subscription
import model.api.{CreateSubscription, UpdateSubscription}
import model.error.ServiceError.DatabaseError
import model.error.ServiceError.SubscriptionNotFoundError
final case class SubscriptionService(
  config: AppConfig,
  repository: SubscriptionRepository,
  producer: SubscriptionsProducer
):
  def create(createSubscription: CreateSubscription): ZIO[Any, DatabaseError, Subscription] =
    for {
      subscription <- Subscription.from(createSubscription)
      _            <- ZIO.logInfo(s"Creating subscription ${subscription.id}")
      _ <- repository
             .create(subscription)
             .logError(s"There was an error on attempt to create subscription ${subscription.id}")
             .mapError(_ => DatabaseError())
      _ <- producer.notify(subscription)
    } yield (subscription)

  def list(): ZIO[Any, DatabaseError, List[Subscription]] =
    ZIO.logInfo(s"Listing all subscriptions ") *>
      repository
        .list()
        .logError(s"There was an error on attempt to list subscriptions")
        .mapError(_ => DatabaseError())

  def get(subscriptionId: SubscriptionId): ZIO[Any, DatabaseError | SubscriptionNotFoundError, Subscription] =
    ZIO.logInfo(s"Looking for subscriptions ${subscriptionId}") *>
      repository
        .get(subscriptionId)
        .logError(s"There was an error on attempt to get subscription ${subscriptionId}")
        .mapError(_ => DatabaseError())
        .flatMap {
          case Some(value) => ZIO.succeed(value)
          case None        => ZIO.fail(SubscriptionNotFoundError(subscriptionId))
        }

  def delete(subscriptionId: SubscriptionId): ZIO[Any, DatabaseError | SubscriptionNotFoundError, Subscription] =
    for {
      _ <- ZIO.logInfo(s"Deleting subscription ${subscriptionId}")
      _ <- get(subscriptionId)
      subscription <- repository
                        .delete(subscriptionId)
                        .logError(s"There was an error on attempt to get subscription ${subscriptionId}")
                        .mapError(_ => DatabaseError())
      _ <- producer.notify(subscription)
    } yield subscription

  def update(
    subscriptionId: SubscriptionId,
    name: Name,
    email: Email
  ): ZIO[Any, DatabaseError | SubscriptionNotFoundError, Subscription] =
    for {
      _ <- ZIO.logInfo(s"Updating subscription ${subscriptionId}")
      _ <- get(subscriptionId)
      subscription <-
        repository
          .update(subscriptionId, name, email)
          .logError(s"There was an error on attempt to update subscription ${subscriptionId}")
          .mapError(_ => DatabaseError())
      _ <- producer.notify(subscription)
    } yield subscription

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
