package com.lamoroso.example
package server

import zio.*
import zio.Random
import zio.ZLayer
import zio.http.App
import zio.http.{Server, ServerConfig}

import sttp.tapir.model.ServerRequest
import sttp.tapir.server.interceptor.RequestInterceptor.RequestResultEffectTransform
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.server.interceptor.{RequestInterceptor, RequestResult}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.*

import com.lamoroso.example.config.AppConfig
import com.lamoroso.example.server.http.endpoints.SubscriptionEndpoints
import com.lamoroso.example.server.http.routes.SubscriptionRoute
import com.lamoroso.example.services.SubscriptionService
import database.Migrations
import server.http.endpoints.SubscriptionEndpoints.*
import server.http.routes.SubscriptionRoute.createSubscriptionServerEndpoint

final case class SubscriptionServer(
  config: AppConfig,
  migrations: Migrations
):
  type RoutesEnv = SubscriptionService

  //TODO: Add middleware to:
  // - log error responses and defects

  lazy val requestIdInterceptor = RequestInterceptor.transformResultEffect(new RequestResultEffectTransform[Task] {
    override def apply[B](request: ServerRequest, result: Task[RequestResult[B]]): Task[RequestResult[B]] =
      Random.nextUUID.flatMap { requestId =>
        ZIO.logAnnotate("rid", requestId.toString) {
          result
        }
      }
  })

  val decodeFailureHandler = DefaultDecodeFailureHandler.default.copy(
    respond = DefaultDecodeFailureHandler.respond(
      _,
      badRequestOnPathErrorIfPathShapeMatches = true,
      badRequestOnPathInvalidIfPathShapeMatches = true
    )
  )

  //Docs
  lazy val swaggerEndpoints =
    SwaggerInterpreter()
      .fromEndpoints[Task](SubscriptionEndpoints.all, "Subscriptions app", "1.0")

  //  Build all server routes
  lazy val routes: App[RoutesEnv] =
    ZioHttpInterpreter(
      ZioHttpServerOptions.customiseInterceptors
        .prependInterceptor(requestIdInterceptor)
        .decodeFailureHandler(decodeFailureHandler)
        .options
    )
      .toApp(
        SubscriptionRoute.all.map(_.widen[RoutesEnv])
          ++ swaggerEndpoints.map(_.widen[RoutesEnv])
      )

  def start =
    for {
      _ <- ZIO.logInfo(s"Starting migrations ...")
      _ <- migrations.migrate
      _ <- ZIO.logInfo(s"Starting docs service at: http://localhost:${config.server.port}/docs ...")
      _ <- Server
             .serve(routes)
    } yield ()

object SubscriptionServer:

  val layer = ZLayer.fromFunction(SubscriptionServer.apply _)

  val serverConfigLayer =
    ZLayer.fromZIO(
      ZIO.service[AppConfig].map(config => ServerConfig.default.port(config.server.port))
    )
