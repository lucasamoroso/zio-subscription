package com.lamoroso.example
package server
package routes

import zio.*

import zio.json.*

import zhttp.http.*

import model.Subscription
import model.api.CreateSubscription
import model.error.*
import server.ServerUtils.*
import services.SubscriptionService

final case class SubscriptionRoute(service: SubscriptionService):
  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    // Create a new  subscription and return it as JSON.
    case req @ Method.POST -> !! / "subscriptions" => {
      (for {
        createSubscription <- parseBody[CreateSubscription](req)
        subscription <-
          service.create(createSubscription)
      } yield Response.json(subscription.toJson)).catchSome {
        case serdeError: SerDeError =>
          ZIO.succeed(Response.json(serdeError.toJson).setStatus(Status.BadRequest))
        case serviceError: ServiceError =>
          ZIO.succeed(Response.json(serviceError.toJson).setStatus(Status.InternalServerError))
      }
    }
    //Return all subscriptions
    case Method.GET -> !! / "subscriptions" =>
      service
        .list()
        .map(subscriptions => Response.json(subscriptions.toJson))
        .catchSome { case serviceError: ServiceError =>
          ZIO.succeed(Response.json(serviceError.toJson).setStatus(Status.InternalServerError))
        }

  }

object SubscriptionRoute:

  val layer = ZLayer.fromFunction(SubscriptionRoute.apply _)
