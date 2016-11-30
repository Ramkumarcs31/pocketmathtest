package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import main.PocketMath

import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationController @Inject()(pocketMath: PocketMath) extends Controller {

  def traders(City: Option[String]): Action[AnyContent] = Action.async {
    pocketMath.getTraders(City) map {
      case Some(traders) =>
        Ok(Json.obj("status" -> Json.obj("code" -> 100, "msg" -> "Got Data"), "data" -> traders))
      case None =>
        NotFound(Json.obj("status" -> Json.obj("code" -> 101, "msg" -> "Data Not Found")))
    } recover {
      case t: Throwable =>
        t.printStackTrace
        InternalServerError(Json.obj("status" -> Json.obj("code" -> 404, "msg" -> "Failed to retrieve Data")))
    }
  }

  def transactions(Year: Option[Int]): Action[AnyContent] = Action.async {
    pocketMath.getTransactions(Year) map {
      case Some(transactions) =>
        Ok(Json.obj("status" -> Json.obj("code" -> 100, "msg" -> "Got Data"), "data" -> transactions))
      case None =>
        NotFound(Json.obj("status" -> Json.obj("code" -> 101, "msg" -> "Data Not Found")))
    } recover {
      case t: Throwable =>
        t.printStackTrace
        InternalServerError(Json.obj("status" -> Json.obj("code" -> 404, "msg" -> "Failed to retrieve Data")))
    }
  }

  def avgTransactions(City: Option[String]): Action[AnyContent] = Action.async {
    pocketMath.getTransactionsAvg(City) map {
      case Some(transactions) =>
        Ok(Json.obj("status" -> Json.obj("code" -> 100, "msg" -> "Got Data"), "data" -> transactions))
      case None =>
        NotFound(Json.obj("status" -> Json.obj("code" -> 101, "msg" -> "Data Not Found")))
    } recover {
      case t: Throwable =>
        t.printStackTrace
        InternalServerError(Json.obj("status" -> Json.obj("code" -> 404, "msg" -> "Failed to retrieve Data")))
    }
  }

  def transaction(value: String): Action[AnyContent] = Action.async {
    pocketMath.getTransaction(value) map {
      case Some(transaction) =>
        Ok(Json.obj("status" -> Json.obj("code" -> 100, "msg" -> "Got Data"), "data" -> transaction))
      case None =>
        NotFound(Json.obj("status" -> Json.obj("code" -> 101, "msg" -> "Data Not Found")))
    } recover {
      case t: Throwable =>
        t.printStackTrace
        InternalServerError(Json.obj("status" -> Json.obj("code" -> 404, "msg" -> "Failed to retrieve Data")))
    }
  }

}
