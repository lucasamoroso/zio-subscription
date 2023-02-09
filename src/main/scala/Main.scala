package com.lamoroso.example

import zio.*

import zio.config.*

import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers

import com.lamoroso.example.config.AppConfig
import database.Migrations
import database.QuillContext
import database.repositories.SubscriptionRepository
import server.SubscriptionServer
import server.routes.SubscriptionRoute
import services.SubscriptionService

object Main extends ZIOAppDefault:

  override def run: ZIO[Any, Any, Any] =
    ZIO
      .serviceWithZIO[SubscriptionServer](_.start)
      .provide(
        SubscriptionServer.layer,
        SubscriptionRoute.layer,
        SubscriptionRepository.layer,
        SubscriptionService.layer,
        Migrations.layer,
        QuillContext.dataSourceLayer,
        SLF4J.slf4j,
        removeDefaultLoggers,
        configLayer_(AppConfig.layer)
      )
