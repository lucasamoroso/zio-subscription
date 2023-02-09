package com.lamoroso.example
package config

import zio.config.*
import zio.config.magnolia.{describe, descriptor}
import zio.config.typesafe.TypesafeConfigSource

import java.io.File

import config.{DatabaseConfig, ServerConfig}

final case class AppConfig(server: ServerConfig, database: DatabaseConfig)

object AppConfig:
  val layer: ConfigDescriptor[AppConfig] = {
    val source = TypesafeConfigSource.fromHoconFile(new File("src/main/resources/application.conf"))
    descriptor[AppConfig] from source
  }
