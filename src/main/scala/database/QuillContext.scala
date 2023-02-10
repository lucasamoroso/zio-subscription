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

import com.lamoroso.example.config.{AppConfig, DatabaseConfig}
import com.lamoroso.example.model.SubscriptionId
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
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
    MappedEncoding[SubscriptionId, UUID](v => UUID.fromString(v.value))

  implicit val decodeSubscriptionIdUUID: MappedEncoding[UUID, SubscriptionId] =
    MappedEncoding[UUID, SubscriptionId] { uuid =>
      SubscriptionId.from(uuid) match
        case Success(id)        => id
        case Failure(exception) => throw exception
    }

  implicit val nameEncoder: MappedEncoding[IronType[String, Name], String] =
    MappedEncoding[IronType[String, Name], String](a => a)

  implicit val nameDecoder: MappedEncoding[String, IronType[String, Name]] =
    MappedEncoding[String, IronType[String, Name]](a => a.refine)
