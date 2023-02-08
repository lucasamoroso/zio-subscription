package com.lamoroso.example
package server

import zio.ZIO
import zio.ZLayer

import zhttp.http.*
import zhttp.service.Server

import config.AppConfig
import server.routes.SubscriptionRoute

final class SubscriptionServer(
  config: AppConfig,
  subscriptionRoute: SubscriptionRoute
):
  val allRoutes: HttpApp[Any, Throwable] = subscriptionRoute.routes

  //TODO: log requests ids

  def start: ZIO[Any, Throwable, Unit] =
    for {
      _ <- ZIO.logInfo(s"Starting server at: http://localhost:${config.server.port}/")
      _ <- Server
             .start(config.server.port, allRoutes)
    } yield ()

object SubscriptionServer:

  def mk(config: AppConfig, subscriptionRoute: SubscriptionRoute) =
    new SubscriptionServer(config, subscriptionRoute)

  val layer = ZLayer.fromFunction(SubscriptionServer.mk _)
