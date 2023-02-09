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
      _ <- repository
             .create(subscription)
             .logError(s"There was an error on attempt to create subscription ${subscription.id}")
             .mapError(_ => DatabaseError())
    } yield (subscription)

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
