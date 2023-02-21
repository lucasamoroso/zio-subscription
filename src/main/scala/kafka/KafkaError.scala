package com.lamoroso.example
package kafka

import zio.json.*

sealed trait KafkaError(message: String) extends Throwable

object KafkaError:

  final case class KafkaProducerError(message: String) extends KafkaError(message)

  object KafkaProducerError:
    implicit val codec: JsonCodec[KafkaProducerError] =
      DeriveJsonCodec.gen[KafkaProducerError]
