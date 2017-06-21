/*
 * Copyright (C) 2011-2017 Interfaculty Department of Geoinformatics, University of
 * Salzburg (Z_GIS) & Institute of Geological and Nuclear Sciences Limited (GNS Science)
 * in the SMART Aquifer Characterisation (SAC) programme funded by the New Zealand
 * Ministry of Business, Innovation and Employment (MBIE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.smart.models.owc100

import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

import com.typesafe.scalalogging.LazyLogging
import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

class TemporalExtentFormat extends Format[List[OffsetDateTime]] with LazyLogging {

  def reads(json: JsValue): JsResult[List[OffsetDateTime]] = {

    json match {
      case jsVal: JsString => parseDateJson(jsVal.toString()) match {
        case Success(date) if date.nonEmpty => {
          JsSuccess(date)
        }
        case Failure(ex) => {
          logger.error(s"JsError ValidationError ${ex.getLocalizedMessage}")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.iso8601date"),
            ValidationError("error.expected.iso8601interval"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.datestring")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.datestring"))))
      }
    }
  }

  def parseDateJson(isoTemporalString: String): Try[List[OffsetDateTime]] = {
    if (isoTemporalString.contains("/")) {
      parseDateAsIntervalJson(isoTemporalString)
    } else {
      parseDateAsDateTimeJson(isoTemporalString)
    }
  }

  def parseDateAsIntervalJson(isoTemporalString: String): Try[List[OffsetDateTime]] = Try {
    val dateStrings = isoTemporalString.replace("\"", "").trim.split("/").toList
    val jsDates = for {
      jsd1 <- JsString(dateStrings.head).validate[OffsetDateTime]
      jsd2 <- JsString(dateStrings.last).validate[OffsetDateTime]
    } yield (jsd1, jsd2)
    jsDates match {
      case JsSuccess(a, b) => List(a._1, a._2)
      case JsError(errors) => {
        logger.error("JsError ValidationError error.expected.iso8601interval", errors)
        throw new DateTimeParseException("JsError ValidationError error.expected.iso8601interval", isoTemporalString, 0)
      }
    }
  }

  def parseDateAsDateTimeJson(isoTemporalString: String): Try[List[OffsetDateTime]] = Try {
    val jsDate = JsString(isoTemporalString.replace("\"", "").trim).validate[OffsetDateTime]
    jsDate match {
      case JsSuccess(date, path) =>  List(date)
      case JsError(errors) => {
        logger.error("JsError ValidationError error.expected.iso datestring", errors)
        throw new DateTimeParseException("JsError ValidationError error.expected.isodatestring", isoTemporalString, 0)
      }
    }
  }

  def writes(dateRange: List[OffsetDateTime]): JsValue = {
    val dates = dateRange.sortWith((a, b) => a.isBefore(b))
    if (dates.head == dates.last) {
      Json.toJson(dates.head)
    } else {
      val d1 = Json.toJson(dates.head)
      val d2 = Json.toJson(dates.last)
      JsString(d1.as[String] + "/" + d2.as[String])
    }
  }
}
