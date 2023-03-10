package com.lamoroso.example
package database

import zio._

import zio.config.*

import io.getquill.MappedEncoding
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{PostgresZioJdbcContext, SnakeCase}

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import com.typesafe.config.ConfigFactory

import scala.jdk.CollectionConverters.MapHasAsJava
import scala.util.Failure
import scala.util.Success

import java.util.Properties
import java.util.UUID

import javax.sql.DataSource

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

import com.lamoroso.example.config.{AppConfig, DatabaseConfig}
import model.RefinedTypes.*

object QuillContext extends PostgresZioJdbcContext(SnakeCase):

  private def dataSourceFrom(config: DatabaseConfig) = ZIO.attempt {
    val props = new Properties()

    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
    props.setProperty("dataSource.user", config.user)
    props.setProperty("dataSource.password", config.password)
    props.setProperty("dataSource.url", config.url)

    new HikariDataSource(new HikariConfig(props))
  }

  val dataSourceLayer: ZLayer[AppConfig, Throwable, DataSource] = ZLayer {
    for {
      appConfig  <- getConfig[AppConfig]
      dbConfig    = appConfig.database
      dataSource <- dataSourceFrom(dbConfig)
    } yield DataSourceLayer.fromDataSource(dataSource)
  }.flatten

  implicit val encodeUUID: MappedEncoding[UUID, String] = MappedEncoding[UUID, String](_.toString)
  implicit val decodeUUID: MappedEncoding[String, UUID] = MappedEncoding[String, UUID](UUID.fromString(_))

  implicit val encodeSubscriptionIdUUID: MappedEncoding[SubscriptionId, UUID] =
    MappedEncoding[SubscriptionId, UUID](v => UUID.fromString(v))

  implicit val decodeSubscriptionIdUUID: MappedEncoding[UUID, SubscriptionId] =
    MappedEncoding[UUID, SubscriptionId](uuid => uuid.asSubscriptionId)

  implicit val nameEncoder: MappedEncoding[Name, String] =
    MappedEncoding[Name, String](identity)

  implicit val nameDecoder: MappedEncoding[String, Name] =
    MappedEncoding[String, Name](_.refine)

  implicit val emailEncoder: MappedEncoding[Email, String] =
    MappedEncoding[Email, String](identity)

  implicit val emailDecoder: MappedEncoding[String, Email] =
    MappedEncoding[String, Email](_.refine)
