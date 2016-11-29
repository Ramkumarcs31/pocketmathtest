package models

import play.api.libs.json.Json

case class Trader(id: String,
                  name: String,
                  city: String)

object Trader {
  implicit val format = Json.format[Trader]
}
