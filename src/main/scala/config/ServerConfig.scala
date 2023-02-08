package com.lamoroso.example
package config

import zio.config.magnolia.{describe, descriptor}

final case class ServerConfig(@describe("The port where the server will start") port: Int)
