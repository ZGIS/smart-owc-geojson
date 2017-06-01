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
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
  * + code :URI
  * + operations :Offering [0..*]
  * + contents :Content [0..*]
  * + styles :StyleSet [0..*]
  * + extension :Any [0..*]
  */
case class OwcOffering(
                        code: URL,
                        operations: List[OwcOperation],
                        contents: List[OwcContent],
                        styles: List[OwcStyleSet],
                        uuid: UUID = UUID.randomUUID()
                      ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcOffering.owc100OfferingFormat)
}

object OwcOffering extends LazyLogging {

  private val owc100OfferingReads: Reads[OwcOffering] = (
    (JsPath \ "code").read[URL](new UrlFormatOfferingExtensions) and
      ((JsPath \ "operations").read[List[OwcOperation]] orElse Reads.pure(List[OwcOperation]())) and
      ((JsPath \ "contents").read[List[OwcContent]] orElse Reads.pure(List[OwcContent]())) and
      ((JsPath \ "styles").read[List[OwcStyleSet]] orElse Reads.pure(List[OwcStyleSet]())) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcOffering.apply _).map { owc =>
    val distinctStyles = owc.styles.map(sty => (sty.name, sty)).toMap.values.toList
    owc.copy(styles = distinctStyles)
  }

  private val owc100OfferingWrites: Writes[OwcOffering] = (
    (JsPath \ "code").write[URL](new UrlFormat) and
      (JsPath \ "operations").write[List[OwcOperation]] and
      (JsPath \ "contents").write[List[OwcContent]] and
      (JsPath \ "styles").write[List[OwcStyleSet]] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcOffering.unapply))

  implicit val owc100OfferingFormat: Format[OwcOffering] = Format(owc100OfferingReads, owc100OfferingWrites)
}
