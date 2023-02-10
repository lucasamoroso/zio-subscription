package com.lamoroso.example
package model

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.zioJson.given

object RefinedTypes:
  type Name = (Alphanumeric & MinLength[3] & MaxLength[20]) DescribedAs
    "Name should be alphanumeric and have a length between 3 and 20"
