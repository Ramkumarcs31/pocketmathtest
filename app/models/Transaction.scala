package models

import play.api.libs.json.Json

case class Transaction(traderId: String,
                       timestamp: Long,
                       value: Double)

object Transaction {
  implicit val format = Json.format[Transaction]
}

