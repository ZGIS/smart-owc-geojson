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

import java.time.ZonedDateTime

import com.typesafe.scalalogging.LazyLogging
import info.smart.models.owc.{JtsPolygonReader, RectangleWriter}
import org.locationtech.spatial4j.shape.Rectangle
import play.api.libs.json.{Reads, Writes}

/**
  * + id :CharacterString
  * + title :CharacterString
  * + abstract :CharacterString [0..1]
  * + updateDate :TM_Date [0..1]
  * + author :CharacterString [0..*]
  * + publisher :CharacterString [0..1]
  * + rights :CharacterString [0..1]
  * + geospatialExtent :GM_Envelope [0..1]
  * + temporalExtent :TM_GeometricPrimitive [0..1]
  * + contentDescription :Any [0..1] // aka alternates
  * + preview :URI [0..*]
  * + contentByRef :URI [0..*]
  * + offering :Offering [0..*]
  * + active :Boolean [0..1]
  * + keyword :CharacterString [0..*]
  * + maxScaleDenominator :double [0..1]
  * + minScaleDenominator :double [0..1]
  * + folder :CharacterString [0..1]
  * + extension :Any [0..*]
  */
case class OwcResource(
                        id: String,
                        title: String,
                        subtitle: Option[String],
                        updateDate: ZonedDateTime,
                        author: List[String],
                        publisher: Option[String],
                        rights: Option[String],
                        geospatialExtent: Option[Rectangle],
                        temporalExtent: Option[List[ZonedDateTime]],
                        contentDescription: List[OwcLink], // alternates
                        preview: List[OwcLink], // aka rel=previews or icon (atom)
                        contentByRef: List[OwcLink], // aka rel=data or enclosure (atom)
                        offering: List[OwcOffering],
                        active: Option[Boolean],
                        resourceMetadata: List[OwcLink], // aka rel=via
                        keyword: List[OwcCategory],
                        minScaleDenominator: Option[Double],
                        maxScaleDenominator: Option[Double],
                        folder: Option[String]
                      ) extends LazyLogging {
}

object OwcResource extends LazyLogging {

  implicit val rectangleWrites: Writes[Rectangle] = new RectangleWriter()
  implicit val rectangleReads: Reads[Option[Rectangle]] = new JtsPolygonReader()

}