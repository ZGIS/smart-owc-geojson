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
import java.time.OffsetDateTime

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.shape.Rectangle
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

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
                        id: URL,
                        title: String,
                        subtitle: Option[String],
                        updateDate: OffsetDateTime,
                        author: List[OwcAuthor],
                        publisher: Option[String],
                        rights: Option[String],
                        geospatialExtent: Option[Rectangle],
                        temporalExtent: Option[List[OffsetDateTime]],
                        contentDescription: List[OwcLink], // links.alternates[] and rel=alternate
                        preview: List[OwcLink], // aka links.previews[] and rel=icon (atom)
                        contentByRef: List[OwcLink], // aka links.data[] and rel=enclosure (atom)
                        offering: List[OwcOffering],
                        active: Option[Boolean],
                        resourceMetadata: List[OwcLink], // aka links.via[] & rel=via
                        keyword: List[OwcCategory],
                        minScaleDenominator: Option[Double],
                        maxScaleDenominator: Option[Double],
                        folder: Option[String]
                      ) extends LazyLogging {

  // write the "type": "Feature" without using a field in the case class
  private val addFeatureTypeJsonTransform = (JsPath \ "type").json.put(JsString("Feature"))

  def toJson: JsValue = {
    val js = Json.toJson(this)
    js.transform(addFeatureTypeJsonTransform).getOrElse(js)
  }
}

object OwcResource extends LazyLogging {

  private val verifyingFeatureTypeReads = new Reads[String] {
    def reads(json: JsValue): JsResult[String] = json.validate[String].flatMap{
      case s if s.equals("Feature") => JsSuccess(s)
      case s => {
        logger.error("JsError ValidationError error.expected.type=feature")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.type=feature"))))
      }
    }
  }

  // read validate the "type": "Feature" without using a field in the case class
  private val readFeatureTypeJsonTransform: Reads[String] = (JsPath \ "type").read[String](verifyingFeatureTypeReads)

  private val owc100ResourceReads: Reads[OwcResource] = (
    (JsPath \ "id").read[URL](new UrlFormat) and
      (JsPath \ "properties" \ "title").read[String](minLength[String](1)) and
      (JsPath \ "properties" \ "abstract").readNullable[String](minLength[String](1)) and
      (JsPath \ "properties" \ "updated").read[OffsetDateTime] and
      ((JsPath \ "properties" \ "authors" ).read[List[OwcAuthor]] orElse Reads.pure(List[OwcAuthor]())) and
      (JsPath \ "properties" \ "publisher").readNullable[String](minLength[String](1)) and
      (JsPath \ "properties" \ "rights").readNullable[String](minLength[String](1)) and
      (JsPath \ "geometry").readNullable[Rectangle](new RectangleGeometryFormat) and
      (JsPath \ "properties" \ "date").readNullable[List[OffsetDateTime]](new TemporalExtentFormat) and
      ((JsPath \ "properties" \ "links" \ "alternates").read[List[OwcLink]] orElse Reads.pure(List[OwcLink]())) and
      ((JsPath \ "properties" \ "links" \ "previews").read[List[OwcLink]] orElse Reads.pure(List[OwcLink]())) and
      ((JsPath \ "properties" \ "links" \ "data").read[List[OwcLink]] orElse Reads.pure(List[OwcLink]())) and
      ((JsPath \ "properties" \ "offerings").read[List[OwcOffering]] orElse Reads.pure(List[OwcOffering]())) and
      (JsPath \ "properties" \ "active").readNullable[Boolean] and
      ((JsPath \ "properties" \ "links" \ "via").read[List[OwcLink]] orElse Reads.pure(List[OwcLink]())) and
      ((JsPath \ "properties" \ "categories" ).read[List[OwcCategory]] orElse Reads.pure(List[OwcCategory]())) and
      (JsPath \ "properties" \ "minscaledenominator").readNullable[Double](min[Double](0)) and
      (JsPath \ "properties" \ "maxscaledenominator").readNullable[Double](min[Double](0)) and
      (JsPath \ "properties" \ "folder").readNullable[String](minLength[String](1))
    ) (OwcResource.apply _)

  // read and validate first Reads[String] and then second Reads[OwcResource and only keep second result
  implicit val owc100validatedResourceReads: Reads[OwcResource] = readFeatureTypeJsonTransform andKeep owc100ResourceReads

  implicit val owc100ResourceWrites: Writes[OwcResource] = (
    (JsPath \ "id").write[URL](new UrlFormat) and
      (JsPath \ "properties" \ "title").write[String] and
      (JsPath \ "properties" \ "abstract").writeNullable[String] and
      (JsPath \ "properties" \ "updated").write[OffsetDateTime] and
      (JsPath \ "properties" \ "authors").write[List[OwcAuthor]] and
      (JsPath \ "properties" \ "publisher").writeNullable[String] and
      (JsPath \ "properties" \ "rights").writeNullable[String] and
      (JsPath \ "geometry").writeNullable[Rectangle](new RectangleGeometryFormat) and
      (JsPath \ "properties" \ "date").writeNullable[List[OffsetDateTime]](new TemporalExtentFormat) and
      (JsPath \ "properties" \ "links" \ "alternates").write[List[OwcLink]] and
      (JsPath \ "properties" \ "links" \ "previews").write[List[OwcLink]] and
      (JsPath \ "properties" \ "links" \ "data").write[List[OwcLink]] and
      (JsPath \ "properties" \ "offerings").write[List[OwcOffering]] and
      (JsPath \ "properties" \ "active").writeNullable[Boolean] and
      (JsPath \ "properties" \ "links" \ "via").write[List[OwcLink]] and
      (JsPath \ "properties" \ "categories").write[List[OwcCategory]] and
      (JsPath \ "properties" \ "minscaledenominator").writeNullable[Double] and
      (JsPath \ "properties" \ "maxscaledenominator").writeNullable[Double] and
      (JsPath \ "properties" \ "folder").writeNullable[String]
    ) (unlift(OwcResource.unapply))

  implicit val owc100OfferingFormat: Format[OwcResource] = Format(owc100validatedResourceReads, owc100ResourceWrites)

}