package com.lamoroso.example
package server.endpoints

import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*

import model.Subscription
import model.SubscriptionId
import model.api.CreateSubscription
import model.error.*
import model.error.ServiceError.DatabaseError
import model.error.ServiceError.SubscriptionNotFoundError
import server.utils.TapirComponents.*
import sttp.model.StatusCode

object SubscriptionEndpoints:
  //Must be a lazy val
  lazy val all = List(createSubscriptionEndpoint, listSubscriptionsEndpoint, getSubscriptionEndpoint)

  val createSubscriptionEndpoint =
    endpoint.post
      .description("This endpoint allows to create a new subscription in our system")
      .in("subscriptions")
      .in(jsonBody[CreateSubscription].description("The request body to create a subscription"))
      .out(jsonBody[Subscription].description("The subscription saved in our system"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError]))
        )
      )
  val listSubscriptionsEndpoint =
    endpoint.get
      .description("List all subscriptions saved in our system")
      .in("subscriptions")
      .out(jsonBody[List[Subscription]].description("All subscriptions in our system"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError]))
        )
      )

  val getSubscriptionEndpoint =
    endpoint.get
      .in(
        "subscriptions" / path[SubscriptionId]("subscriptionId")
          .description("The subscription id from the desired subscription")
      )
      .description("Find a subscription that match the provided subscription id")
      .out(jsonBody[Subscription].description("The desired subscription if it was found in our systems"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError])),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[SubscriptionNotFoundError]))
        )
      )
