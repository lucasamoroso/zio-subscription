package com.lamoroso.example
package database.repositories

import zio.IO
import zio.ZEnvironment
import zio.ZIO
import zio.ZLayer

import io.getquill.*
import io.getquill.jdbczio.Quill.DataSource

import java.sql.SQLException

import javax.sql.DataSource

import database.QuillContext
import io.github.iltotore.iron.*
import model.RefinedTypes.*
import model.Subscription
import model.SubscriptionId

final case class SubscriptionRepository(dataSource: DataSource):
  import QuillContext.*

  def create(subscription: Subscription): ZIO[Any, SQLException, Unit] =
    for {
      _ <- run(query[Subscription].insertValue(lift(subscription))).provideEnvironment(ZEnvironment(dataSource))
      _ <- ZIO.logInfo(s"Subscription ${subscription.id} saved")
    } yield ()

  def list(): ZIO[Any, SQLException, List[Subscription]] =
    run(query[Subscription])
      .provideEnvironment(ZEnvironment(dataSource))

  def get(subscriptionId: SubscriptionId): ZIO[Any, SQLException, Option[Subscription]] =
    run(query[Subscription].filter(_.id == lift(subscriptionId)))
      .provideEnvironment(ZEnvironment(dataSource))
      .map(_.headOption)

  def delete(subscriptionId: SubscriptionId): ZIO[Any, SQLException, Subscription] =
    run(query[Subscription].filter(_.id == lift(subscriptionId)).delete.returning(r => r))
      .provideEnvironment(ZEnvironment(dataSource))

  def update(id: SubscriptionId, name: Name, email: String): IO[SQLException, Subscription] =
    run(
      query[Subscription]
        .filter(_.id == lift(id))
        .update(_.name -> lift(name), _.email -> lift(email))
        .returning(s => s)
    ).provideEnvironment(ZEnvironment(dataSource))

object SubscriptionRepository:

  val layer = ZLayer.fromFunction(SubscriptionRepository.apply _)
