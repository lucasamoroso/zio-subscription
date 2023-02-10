package com.lamoroso.example
package services

import zio.UIO
import zio.ZIO
import zio.ZLayer

import java.sql.SQLException

import com.lamoroso.example.model.SubscriptionId
import database.repositories.SubscriptionRepository
import model.Subscription
import model.api.CreateSubscription
import model.error.ServiceError.DatabaseError
import model.error.ServiceError.SubscriptionNotFoundError

final case class SubscriptionService(repository: SubscriptionRepository):
  def create(createSubscription: CreateSubscription): ZIO[Any, DatabaseError, Subscription] =
    for {
      subscription <- Subscription.from(createSubscription)
      _            <- ZIO.logInfo(s"Creating subscription ${subscription.id}")
      _ <- repository
             .create(subscription)
             .logError(s"There was an error on attempt to create subscription ${subscription.id}")
             .mapError(_ => DatabaseError())
    } yield (subscription)

  def list(): ZIO[Any, DatabaseError, List[Subscription]] =
    ZIO.logInfo(s"Listing all subscriptions ") *>
      repository
        .list()
        .logError(s"There was an error on attempt to list subscriptions")
        .mapError(_ => DatabaseError())

  def get(subscriptionId: SubscriptionId): ZIO[Any, DatabaseError | SubscriptionNotFoundError, Subscription] =
    ZIO.logInfo(s"Looking for subscriptions ${subscriptionId.value}") *>
      repository
        .get(subscriptionId)
        .logError(s"There was an error on attempt to get subscription ${subscriptionId}")
        .mapError(_ => DatabaseError())
        .flatMap {
          case Some(value) => ZIO.succeed(value)
          case None        => ZIO.fail(SubscriptionNotFoundError(subscriptionId))
        }

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
