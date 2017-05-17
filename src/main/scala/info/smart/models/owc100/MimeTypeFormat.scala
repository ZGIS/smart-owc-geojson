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

import com.typesafe.scalalogging.LazyLogging
import play.api.data.validation.ValidationError
import play.api.libs.json._

class MimeTypeFormat extends Format[String] with LazyLogging {

  def validateMimeType(mimeType: String): Option[String] = MimeTypes.isKnown(mimeType)

  def reads(json: JsValue): JsResult[String] = {

    json match {
      case JsString(mimeType) => validateMimeType(mimeType) match {
        case Some(s) => JsSuccess(s)
        case None => {
          logger.error("JsError ValidationError error.expected.validmimetype")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.validmimetype"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.jsstring")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
      }
    }
  }

  def writes(mimeType: String): JsValue = {
    Json.toJson(mimeType)
  }
}