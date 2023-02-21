package com.lamoroso.example
package config

import zio.config.magnolia.describe

final case class KafkaConfig(
  @describe("The urls to reach the kafka servers")
  brokers: List[String],
  @describe("The name of the subscription topic, this will be used to notify about changes in subscriptions")
  subscriptionTopic: String
)
