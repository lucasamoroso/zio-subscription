package com.lamoroso.example
package model

import java.util.UUID

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.zioJson.given

object RefinedTypes:

  type Name           = String :| ValidName
  type Email          = String :| ValidEmail
  type SubscriptionId = String :| ValidUUID

  private type ValidName = (Alphanumeric & MinLength[3] & MaxLength[20]) DescribedAs
    "Name should be alphanumeric and have a length between 3 and 20"

  private type ValidEmail =
    Match[
      "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
    ] DescribedAs "Should be a valid email"

  extension (uuid: UUID) def asSubscriptionId: String :| ValidUUID = uuid.toString.refine
