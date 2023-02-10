package com.lamoroso.example
package server
package routes

import zio.*

import zio.json.*

import sttp.tapir.ztapir.*

import com.lamoroso.example.server.endpoints.SubscriptionEndpoints.*
import model.RefinedTypes.*
import model.Subscription
import model.api.CreateSubscription
import model.error.RequestError.*
import services.SubscriptionService

object SubscriptionRoute:

  //Must be a lazy val
  lazy val all = List(
    createSubscriptionServerEndpoint,
    listSubscriptionsServerEndpoint,
    getSubscriptionsServerEndpoint,
    deleteSubscriptionsServerEndpoint,
    updateSubscriptionServerEndpoint
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

  val getSubscriptionsServerEndpoint: ZServerEndpoint[SubscriptionService, Any] =
    getSubscriptionEndpoint.zServerLogic { uuid =>
      (for {
        service       <- ZIO.service[SubscriptionService]
        subscriptions <- service.get(uuid.asSubscriptionId)
      } yield subscriptions)
    }

  val deleteSubscriptionsServerEndpoint: ZServerEndpoint[SubscriptionService, Any] =
    deleteSubscriptionEndpoint.zServerLogic { uuid =>
      for {
        service      <- ZIO.service[SubscriptionService]
        subscription <- service.delete(uuid.asSubscriptionId)
      } yield subscription
    }

  val updateSubscriptionServerEndpoint: ZServerEndpoint[SubscriptionService, Any] =
    updateSubscriptionEndpoint.zServerLogic { (uuid, updateSubscription) =>
      for {
        _ <- if (uuid.asSubscriptionId != updateSubscription.id) {
               ZIO.fail(ParamMismatchError(uuid.toString))
             } else {
               ZIO.unit
             }
        service <- ZIO.service[SubscriptionService]
        subscription <-
          service.update(updateSubscription.id, updateSubscription.name, updateSubscription.email)
      } yield subscription
    }
