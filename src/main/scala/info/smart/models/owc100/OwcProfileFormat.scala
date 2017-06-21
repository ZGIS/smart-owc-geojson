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

import com.typesafe.scalalogging.LazyLogging
import play.api.data.validation.ValidationError
import play.api.libs.json._

class OwcProfileFormat extends Format[OwcLink] with LazyLogging {

  override def reads(json: JsValue): JsResult[OwcLink] = {
    json.validate[OwcLink] match {
      case JsSuccess(owcLink, _) => {
        if (SUPPORTED_GEOJSON_PROFILES.contains(owcLink.href)) {
          JsSuccess(owcLink.copy(href = geojsonSpecUrlToGenericSpecUrl(owcLink.href)))
        } else {
          logger.error("JsError ValidationError error.expected.validprofilelink")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.unexpected.failure"))))
        }

      }
      case JsError(errSeq) => {
        logger.error("JsError ValidationError error.expected.owclink")
        JsError(errSeq ++ Seq(JsPath() -> Seq(ValidationError("error.expected.owclink"))))
      }
      case _ => {
        logger.error("JsError ValidationError error.unexpected.failure")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.unexpected.failure"))))
      }
    }
  }

  override def writes(owcLink: OwcLink): JsValue = {
    Json.toJson(owcLink.copy(href = genericSpecUrlToGeojsonSpecUrl(owcLink.href)))
  }
}

class OwcProfileListFormat extends Format[List[OwcLink]] with LazyLogging {

  def reads(json: JsValue): JsResult[List[OwcLink]] = {

    json match {
      case JsArray(jsLinkObjects) => {
        val profiles: List[OwcLink] = jsLinkObjects.map(
          owcLinkJs => {
            owcLinkJs.validate[OwcLink](new OwcProfileFormat) match {
              case JsSuccess(owcLink, jsPath) => Some(owcLink)
              case _ => None
            }
          }
        ).filter(_.isDefined).map(_.get).toList
        val finalCheck = profiles.map(p => p.href)
        if (finalCheck.isEmpty || !finalCheck.contains(new URL(s"$GENERIC_OWC_SPEC_URL/core"))) {
          logger.error("JsError ValidationError error.expected.supportcoreprofile")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.supportcoreprofile"))))
        } else {
          JsSuccess(profiles)
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.arrayobjects")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.arrayobjects"))))
      }
    }
  }

  def writes(owcLinks: List[OwcLink]): JsValue = {
    val profiles = owcLinks.map(o => o.copy(href = genericSpecUrlToGeojsonSpecUrl(o.href)).toJson)
    Json.toJson(JsArray(profiles))
  }
}
