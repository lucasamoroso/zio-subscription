package com.lamoroso.example
package server

import zio.ZIO
import zio.ZLayer

import zhttp.http.*
import zhttp.service.Server

import scala.annotation.migration

import config.AppConfig
import database.Migrations
import server.routes.SubscriptionRoute

final case class SubscriptionServer(
  config: AppConfig,
  subscriptionRoute: SubscriptionRoute,
  migrations: Migrations
):
  val allRoutes: HttpApp[Any, Throwable] = subscriptionRoute.routes

  //TODO: Add middleware to:
  // - log requests ids
  // - log error responses and defects

  def start: ZIO[Any, Throwable, Unit] =
    for {
      _ <- ZIO.logInfo(s"Starting migrations ...")
      _ <- migrations.migrate
      _ <- ZIO.logInfo(s"Starting server at: http://localhost:${config.server.port}/ ...")
      _ <- Server
             .start(config.server.port, allRoutes)
    } yield ()

object SubscriptionServer:

  val layer = ZLayer.fromFunction(SubscriptionServer.apply _)
