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
  * + creatorApplication :CreatorApplication [0..1]
  * + creatorDisplay :CreatorDisplay [0..1]
  * + extension :Any [0..*]
  *
  * OwcCreator base class is never realized, neither in Atom nor in GeoJSON Encoding;
  * Creator/Display and Creator/Application are instantiated as completely separate entities in both Atom and GeoJSON
  */
case class OwcCreator(
                       creatorApplication: Option[OwcCreatorApplication],
                       creatorDisplay: Option[OwcCreatorDisplay]
                     )


/*
CreatorApplication
+ title :CharacterString [0..1]
+ uri :URI [0..1]
+ version :Version [0..1]
+ the single only class that doesn't have explicit extension? (but would be inherited from OWC:Creator?)
*/

case class OwcCreatorApplication(
                                  title: Option[String],
                                  uri: Option[URL],
                                  version: Option[String],
                                  uuid: UUID = UUID.randomUUID()
                                ) extends CustomCopyCompare with LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcCreatorApplication.owc100CreatorApplicationFormat)

  def newOf: OwcCreatorApplication = this.copy(uuid = UUID.randomUUID())

  def customHashCode: Int = (title, uri, version).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcCreatorApplication =>
      this.customHashCode.equals(that.customHashCode)
    case _ => false
  }
}

object OwcCreatorApplication extends LazyLogging {

  def newOf(owcCreatorApplication: OwcCreatorApplication): OwcCreatorApplication = owcCreatorApplication.copy(uuid = UUID.randomUUID())

  private val owc100CreatorApplictionReads: Reads[OwcCreatorApplication] = (
    (JsPath \ "title").readNullable[String](minLength[String](1)) and
      (JsPath \ "uri").readNullable[URL](new UrlFormat) and
      (JsPath \ "version").readNullable[String](minLength[String](1)) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcCreatorApplication.apply _)

  private val owc100OCreatorApplicationWrites: Writes[OwcCreatorApplication] = (
    (JsPath \ "title").writeNullable[String] and
      (JsPath \ "uri").writeNullable[URL](new UrlFormat) and
      (JsPath \ "version").writeNullable[String] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcCreatorApplication.unapply))

  implicit val owc100CreatorApplicationFormat: Format[OwcCreatorApplication] = Format(owc100CreatorApplictionReads, owc100OCreatorApplicationWrites)
}

/*
CreatorDisplay
+ pixelWidth :int [0..1]
+ pixelHeight :int [0..1]
+ mmPerPixel :double [0..1]
+ extension :Any [0..*]
 */

case class OwcCreatorDisplay(
                              pixelWidth: Option[Int],
                              pixelHeight: Option[Int],
                              mmPerPixel: Option[Double],
                              uuid: UUID = UUID.randomUUID()
                            ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcCreatorDisplay.owc100CreatorDisplayFormat)
}

object OwcCreatorDisplay extends LazyLogging {

  private val owc100CreatorDisplayReads: Reads[OwcCreatorDisplay] = (
    (JsPath \ "pixelWidth").readNullable[Int](min[Int](0)) and
      (JsPath \ "pixelHeight").readNullable[Int](min[Int](0)) and
      (JsPath \ "mmPerPixel").readNullable[Double](min[Double](0)) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcCreatorDisplay.apply _)

  private val owc100OCreatorDisplayWrites: Writes[OwcCreatorDisplay] = (
    (JsPath \ "pixelWidth").writeNullable[Int] and
      (JsPath \ "pixelHeight").writeNullable[Int] and
      (JsPath \ "mmPerPixel").writeNullable[Double] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcCreatorDisplay.unapply))

  implicit val owc100CreatorDisplayFormat: Format[OwcCreatorDisplay] = Format(owc100CreatorDisplayReads, owc100OCreatorDisplayWrites)
}
