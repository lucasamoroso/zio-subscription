package com.lamoroso.example
package config

import zio.config.*
import zio.config.magnolia.{describe, descriptor}
import zio.config.typesafe.TypesafeConfigSource

import config.ServerConfig
import java.io.File

final case class AppConfig(server: ServerConfig)

object AppConfig:
  val layer: ConfigDescriptor[AppConfig] = {
    val source = TypesafeConfigSource.fromHoconFile(new File("src/main/resources/application.conf"))
    descriptor[AppConfig] from source
  }
