/*
 * Copyright (c) 2011-2017 Interfaculty Department of Geoinformatics, University of
 * Salzburg (Z_GIS) & Institute of Geological and Nuclear Sciences Limited (GNS Science)
 * in the SMART Aquifer Characterisation (SAC) programme funded by the New Zealand
 * Ministry of Business, Innovation and Employment (MBIE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.smart.models.owc100

import java.net.URL

import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

class OfferingExtensionsUrlFormat extends UrlFormat {

  override def parseURL(url: String): Try[URL] = super.parseURL(url)

  override def writes(url: URL): JsValue = super.writes(genericSpecUrlToGeojsonSpecUrl(url))

  override def reads(json: JsValue): JsResult[URL] = {
    json match {
      case JsString(url) => parseURL(url) match {
        case Success(u) => {
          if (SUPPORTED_GEOJSON_OFFERING_EXTENSIONS.contains(u)) {

            JsSuccess(geojsonSpecUrlToGenericSpecUrl(u))
          } else {
            logger.error("JsError ValidationError error.expected.supportedofferingcode")
            JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.supportedofferingcode"))))
          }
        }
        case Failure(ex) => {
          logger.error("JsError ValidationError error.expected.offeringcodeurl")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.offeringcodeurl"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.jsstring")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
      }
    }
  }
}
