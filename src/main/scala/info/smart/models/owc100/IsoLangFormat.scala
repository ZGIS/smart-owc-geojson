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

import com.typesafe.scalalogging.LazyLogging
import play.api.data.validation.ValidationError
import play.api.libs.json._

import java.util.Locale

class IsoLangFormat extends Format[String] with LazyLogging {

  def validateIsoLangCode(lang: String): Option[String] = Locale.getISOLanguages.toSeq.find(_.equalsIgnoreCase(lang))

  def reads(json: JsValue): JsResult[String] = {

    json match {
      case JsString(lang) => validateIsoLangCode(lang) match {
        case Some(s) => JsSuccess(s.toLowerCase)
        case None => {
          logger.error("JsError ValidationError error.expected.validisolang")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.validisolang"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.jsstring")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
      }
    }
  }

  def writes(lang: String): JsValue = {
    Json.toJson(lang)
  }
}
