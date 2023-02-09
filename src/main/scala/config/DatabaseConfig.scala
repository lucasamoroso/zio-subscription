package com.lamoroso.example
package config

import zio.config.magnolia.{describe, descriptor}

final case class DatabaseConfig(
  @describe("The database user") user: String,
  @describe("The password for the user to access the database") password: String,
  @describe("The url to reach the database") url: String
)
