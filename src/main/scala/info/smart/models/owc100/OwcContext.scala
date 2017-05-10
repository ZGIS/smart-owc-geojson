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
  * + specReference :URI
  * + language :CharacterString
  * + id :CharacterString
  * + title :CharacterString
  * + abstract :CharacterString [0..1]
  * + updateDate :CharacterString [0..1]
  * + author :CharacterString [0..*]
  * + publisher :CharacterString [0..1]
  * + creator :Creator [0..1]
  * + rights :CharacterString [0..1]
  * + areaOfInterest :GM_Envelope [0..1]
  * + timeIntervalOfInterest :TM_GeometricPrimitive [0..1]
  * + keyword :CharacterString [0..*]
  * + extension :Any [0..*]
  */
case class OwcContext(
                       specReference: OwcLink, // aka profile
                       contextMetadata: List[OwcLink], // e.g. via and self
                       resource: List[OwcResource],
                       language: String,
                       id: String,
                       title: String,
                       subtitle: Option[String],
                       updateDate: ZonedDateTime,
                       author: List[String],
                       publisher: Option[String],
                       creator: Option[OwcCreator],
                       rights: Option[String],
                       areaOfInterest: Option[Rectangle],
                       timeIntervalOfInterest: Option[Tuple2[ZonedDateTime, ZonedDateTime]],
                       keyword: List[OwcCategory]
                     ) extends LazyLogging {
}

object OwcContext extends LazyLogging {

  implicit val rectangleWrites: Writes[Rectangle] = new RectangleWriter()
  implicit val rectangleReads: Reads[Option[Rectangle]] = new JtsPolygonReader()

}