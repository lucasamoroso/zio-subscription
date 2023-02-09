package com.lamoroso.example
package server
package routes

import zio.*

import zio.json.*

import zhttp.http.*

import sttp.tapir.ztapir.*

import com.lamoroso.example.server.endpoints.SubscriptionEndpoints.*
import model.Subscription
import model.api.CreateSubscription
import model.error.*
import services.SubscriptionService

object SubscriptionRoute:

  //Must be a lazy val
  lazy val all = List(
    createSubscriptionServerEndpoint,
    listSubscriptionsServerEndpoint
  )

  val createSubscriptionServerEndpoint: ZServerEndpoint[SubscriptionService, Any] =
    createSubscriptionEndpoint.zServerLogic { createSubscription =>
      (for {
        service      <- ZIO.service[SubscriptionService]
        subscription <- service.create(createSubscription)
      } yield subscription)
    }

  val listSubscriptionsServerEndpoint: ZServerEndpoint[SubscriptionService, Any] =
    listSubscriptionsEndpoint.zServerLogic { _ =>
      (for {
        service       <- ZIO.service[SubscriptionService]
        subscriptions <- service.list()
      } yield subscriptions)
    }
