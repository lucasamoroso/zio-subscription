package com.lamoroso.example
package server.endpoints

import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*

import model.Subscription
import model.api.CreateSubscription
import model.error.*
import sttp.model.StatusCode

object SubscriptionEndpoints:
  //Must be a lazy val
  lazy val all = List(createSubscriptionEndpoint, listSubscriptionsEndpoint)

  val createSubscriptionEndpoint =
    endpoint.post
      .in("subscriptions")
      .in(jsonBody[CreateSubscription])
      .out(jsonBody[Subscription])
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[ServiceError]))
        )
      )
  val listSubscriptionsEndpoint =
    endpoint.get
      .in("subscriptions")
      .out(jsonBody[List[Subscription]])
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[ServiceError]))
        )
      )
