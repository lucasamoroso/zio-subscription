package com.lamoroso.example
package database.repositories

import zio.ZEnvironment
import zio.ZIO
import zio.ZLayer

import io.getquill.*
import io.getquill.jdbczio.Quill.DataSource

import java.sql.SQLException

import javax.sql.DataSource

import database.QuillContext
import model.Subscription

final case class SubscriptionRepository(dataSource: DataSource):
  import QuillContext.*

  def create(subscription: Subscription): ZIO[Any, SQLException, Unit] =
    for {
      _ <- run(query[Subscription].insertValue(lift(subscription))).provideEnvironment(ZEnvironment(dataSource))
      _ <- ZIO.logInfo(s"Subscription ${subscription.id} saved")
    } yield ()

object SubscriptionRepository:

  val layer = ZLayer.fromFunction(SubscriptionRepository.apply _)
