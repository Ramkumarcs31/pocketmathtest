package main

import javax.inject.Inject

import models.{Transaction, Trader}
import org.joda.time.DateTime
import play.api.{Logger, Configuration}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class PocketMath @Inject()(config: Configuration, wsClient: WSClient) {

  lazy val PocketMathHost = config.getString("pocketmath.host")
  lazy val TradersEndpoint = config.getString("pocketmath.endpoint.traders")
  lazy val TransactionsEndpoint = config.getString("pocketmath.endpoint.transactions")
  lazy val ApiKey = config.getString("pocketmath.apiKey")

  def  getTraders(City: Option[String]): Future[Option[List[Trader]]] = {
    if (PocketMathHost.isDefined && TradersEndpoint.isDefined && ApiKey.isDefined) {
      val eventualWSResponse = wsClient.url(PocketMathHost.get + TradersEndpoint.get).
        withRequestTimeout(5000 milliseconds).
        withHeaders("x-api-key" -> ApiKey.get).
        get()
      eventualWSResponse map { wsResponse =>
        val Traders: Option[List[Trader]] = Json.parse(wsResponse.json.toString).asOpt[List[Trader]]
        Traders match {
          case Some(traders) =>
            if(City.isDefined) {
              Some(traders.filter(_.city.toLowerCase == City.get.toLowerCase).sortBy(f => f.name))
            } else {
              Some(traders)
            }
          case None =>
            None
        }
      } recover {
        case t: Throwable =>
          Logger.error("exception while fetching data")
          t.printStackTrace
          None
      }
    } else {
      Logger.info("No data found")
      Future.successful(None)
    }
  }

  def getTransactionsAvg(City: Option[String]): Future[Option[Double]] = {
    if (PocketMathHost.isDefined & TransactionsEndpoint.isDefined && ApiKey.isDefined) {
      val eventualResult = getTraders(City) map {
        case Some(traders) =>
          getTransactions(None) map {
            case Some(transactions) =>
              var tradersList = traders.map(_.id)
              if (City.isDefined) {
                tradersList = traders.filter(_.city.toLowerCase == City.get.toLowerCase).map(_.id)
                Some((transactions.filter(p => tradersList.contains(p.traderId)).map(_.value).sum) / tradersList.size)
              } else {
                Some(transactions.map(_.value).sum / transactions.size)
              }
            case None =>
              None
          }
        case None =>
          Future.successful(None)
      } recover {
        case t: Throwable =>
          Logger.error("exception while fetching data")
          t.printStackTrace
          Future.successful(None)
      }
      eventualResult.flatMap(f => f)
    } else {
      Logger.info("No data found")
      Future.successful(None)
    }
  }

  def getTransactions(Year: Option[Int]): Future[Option[List[Transaction]]] = {
    if (PocketMathHost.isDefined & TransactionsEndpoint.isDefined && ApiKey.isDefined) {
      val eventualWSResponse = wsClient.url(PocketMathHost.get + TransactionsEndpoint.get).
        withRequestTimeout(5000 milliseconds).
        withHeaders("x-api-key" -> ApiKey.get).
        get()
      eventualWSResponse map { wsResponse =>
        val Transactions = Json.parse(wsResponse.json.toString).asOpt[List[Transaction]]
        Transactions match {
          case Some(transactions) =>
            if(Year.isDefined) {
              Some(transactions.filter(f => new DateTime(f.timestamp * 1000L).getYear == Year.get).sortBy(-_.value))
            } else {
              Some(transactions)
            }
          case None =>
            None
        }
      } recover {
        case t: Throwable =>
          Logger.error("exception while fetching data")
          t.printStackTrace
          None
      }
    } else {
      Logger.info("No Data found")
      Future.successful(None)
    }
  }

  def getTransaction(value: String): Future[Option[Transaction]] = {
    if (PocketMathHost.isDefined & TransactionsEndpoint.isDefined && ApiKey.isDefined) {
      val eventualWSResponse = wsClient.url(PocketMathHost.get + TransactionsEndpoint.get).
        withRequestTimeout(5000 milliseconds).
        withHeaders("x-api-key" -> ApiKey.get).
        get()
      eventualWSResponse map { wsResponse =>
        val Transactions = Json.parse(wsResponse.json.toString).asOpt[List[Transaction]]
        Transactions match {
          case Some(transactions) =>
            if(value == "high") {
              Some(transactions.sortBy(-_.value).head)
            } else if(value == "low") {
              Some(transactions.sortBy(_.value).head)
            } else {
              None
            }
          case None =>
            None
        }
      } recover {
        case t: Throwable =>
          Logger.error("exception while fetching data")
          t.printStackTrace
          None
      }
    } else {
      Logger.info("No Data found")
      Future.successful(None)
    }
  }
}
