package com.lamoroso.example
package server.endpoints

import sttp.tapir.Endpoint
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*

import java.util.UUID

import model.Subscription
import model.api.{CreateSubscription, UpdateSubscription}
import model.error.*
import model.error.RequestError.*
import model.error.ServiceError.*
import server.utils.TapirComponents.*
import sttp.model.StatusCode

object SubscriptionEndpoints:
  //Must be a lazy val
  lazy val all =
    List(
      createSubscriptionEndpoint,
      listSubscriptionsEndpoint,
      getSubscriptionEndpoint,
      deleteSubscriptionEndpoint,
      updateSubscriptionEndpoint
    )

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
        "subscriptions" / path[UUID]("subscriptionId")
          .description("The subscription id from the desired subscription")
      )
      .description("Find a subscription matching with the provided subscription id")
      .out(jsonBody[Subscription].description("The desired subscription if it was found in our systems"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError])),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[SubscriptionNotFoundError]))
        )
      )

  val deleteSubscriptionEndpoint =
    endpoint.delete
      .in(
        "subscriptions" / path[UUID]("subscriptionId")
          .description("The subscription id from the subscription to delete")
      )
      .description("Delete the subscription matching with the provided subscription id")
      .out(jsonBody[Subscription].description("The deleted subscription if it was found in our systems"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError])),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[SubscriptionNotFoundError]))
        )
      )

  val updateSubscriptionEndpoint =
    endpoint.put
      .in(
        "subscriptions" / path[UUID]("subscriptionId")
          .description("The subscription id from the subscription to update")
      )
      .description("Updates the subscription matching with the provided subscription id")
      .in(
        jsonBody[UpdateSubscription].description(
          "The new values desired for the subscription matching with the subscription id provided"
        )
      )
      .out(jsonBody[Subscription].description("The updated subscription if it was found in our systems"))
      .errorOut(
        oneOf(
          oneOfVariant(statusCode(StatusCode.InternalServerError).and(jsonBody[DatabaseError])),
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[SubscriptionNotFoundError])),
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[ParamMismatchError]))
        )
      )
