package com.lamoroso.example
package database

import zio._

import zio.config.*

import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.{PostgresZioJdbcContext, SnakeCase}

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import com.typesafe.config.ConfigFactory

import scala.jdk.CollectionConverters.MapHasAsJava

import java.util.Properties

import javax.sql.DataSource

import com.lamoroso.example.config.{AppConfig, DatabaseConfig}

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