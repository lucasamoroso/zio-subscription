package com.lamoroso.example
package services

import zio.UIO
import zio.ZIO
import zio.ZLayer

import java.sql.SQLException

import database.repositories.SubscriptionRepository
import model.Subscription
import model.api.CreateSubscription
import model.error.ServiceError.DatabaseError

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

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
