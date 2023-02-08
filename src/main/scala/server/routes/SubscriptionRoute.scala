package com.lamoroso.example
package server
package routes

import zio.*

import zio.json.*

import zhttp.http.*

import model.Subscription

final class SubscriptionRoute:
  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    // Gets all the subscriptions from the database and returns them as JSON.
    case Method.GET -> !! / "subscriptions" =>
      ZIO.attempt(Response.json(Subscription("", "", 1).toJson))
  }

object SubscriptionRoute:

  def mk() = new SubscriptionRoute()

  val layer = ZLayer.fromFunction(SubscriptionRoute.mk _)
