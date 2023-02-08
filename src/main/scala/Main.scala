package com.lamoroso.example

import zio.*

import zio.config.*

import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers

import com.lamoroso.example.config.AppConfig
import server.SubscriptionServer
import server.routes.SubscriptionRoute

object Main extends ZIOAppDefault:

  override def run: ZIO[Any, Any, Any] =
    ZIO
      .serviceWithZIO[SubscriptionServer](_.start)
      .provide(
        SubscriptionServer.layer,
        SubscriptionRoute.layer,
        SLF4J.slf4j,
        removeDefaultLoggers,
        configLayer_(AppConfig.layer)
      )
