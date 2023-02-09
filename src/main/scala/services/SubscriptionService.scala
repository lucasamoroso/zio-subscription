package com.lamoroso.example
package services

import zio.UIO
import zio.ZIO
import zio.ZLayer

import java.sql.SQLException

import database.repositories.SubscriptionRepository
import model.Subscription
import model.api.CreateSubscription

final case class SubscriptionService(repository: SubscriptionRepository):
  //TODO: Error hierarchy
  def create(createSubscription: CreateSubscription): ZIO[Any, SQLException, Subscription] =
    for {
      subscription <- Subscription.from(createSubscription)
      _            <- repository.create(subscription)
    } yield (subscription)

object SubscriptionService:

  val layer = ZLayer.fromFunction(SubscriptionService.apply _)
