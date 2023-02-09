package com.lamoroso.example
package server

import zio.Random
import zio.ZIO
import zio.ZLayer

import zhttp.http.*
import zhttp.http.middleware.HttpMiddleware
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

  /**
   * Logs the requests made to the server.
   *
   * It also adds a request ID to the logging context, so any further logging
   * that occurs in the handler can be associated with the same request.
   *
   * For more information on the logging, see:
   * https://zio.github.io/zio-logging/
   */
  val loggingMiddleware: HttpMiddleware[Any, Nothing] =
    new HttpMiddleware[Any, Nothing] {
      override def apply[R1 <: Any, E1 >: Nothing](
        http: Http[R1, E1, Request, Response]
      ): Http[R1, E1, Request, Response] =
        Http.fromOptionFunction[Request] { request =>
          Random.nextUUID.flatMap { requestId =>
            ZIO.logAnnotate("rid", requestId.toString) {
              http(request)
            }
          }
        }
    }

  def start: ZIO[Any, Throwable, Unit] =
    for {
      _ <- ZIO.logInfo(s"Starting migrations ...")
      _ <- migrations.migrate
      _ <- ZIO.logInfo(s"Starting server at: http://localhost:${config.server.port}/ ...")
      _ <- Server
             .start(config.server.port, allRoutes @@ loggingMiddleware)
    } yield ()

object SubscriptionServer:

  val layer = ZLayer.fromFunction(SubscriptionServer.apply _)
