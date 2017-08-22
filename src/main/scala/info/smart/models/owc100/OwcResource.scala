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
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.shape.Rectangle
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.json._


/**
  * + id :CharacterString - Unambiguous reference to the identification of the resource, SHALL contain an (IRI) URI value

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
                        subtitle: Option[String] = None,
                        updateDate: OffsetDateTime = OffsetDateTime.now(),
                        author: List[OwcAuthor] = List(),
                        publisher: Option[String] = None,
                        rights: Option[String] = None,
                        geospatialExtent: Option[Rectangle] = None,
                        temporalExtent: Option[List[OffsetDateTime]] = None,
                        contentDescription: List[OwcLink] = List(), // links.alternates[] and rel=alternate
                        preview: List[OwcLink] = List(), // aka links.previews[] and rel=icon (atom)
                        contentByRef: List[OwcLink] = List(), // aka links.data[] and rel=enclosure (atom)
                        offering: List[OwcOffering] = List(),
                        active: Option[Boolean] = None,
                        resourceMetadata: List[OwcLink] = List(), // aka links.via[] & rel=via
                        keyword: List[OwcCategory] = List(),
                        minScaleDenominator: Option[Double] = None,
                        maxScaleDenominator: Option[Double] = None,
                        folder: Option[String] = None
                      ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcResource.owc100ResourceFormat)

  def newOf(newId: URL = new URL(this.id.toString + "_copy_" + UUID.randomUUID().toString)): OwcResource =
    this.copy(id = newId,
      author = author.map(o => o.newOf),
      contentDescription = contentDescription.map(o => o.newOf),
      preview = preview.map(o => o.newOf),
      contentByRef = contentByRef.map(o => o.newOf),
      offering = offering.map(o => o.newOf),
      resourceMetadata = resourceMetadata.map(o => o.newOf),
      keyword = keyword.map(o => o.newOf))

  // we intentially leave id away to allow sameAs function to behave as all other ones, compare contents and ignore (uu)ids
  def customHashCode: Int = (
    title,
    subtitle,
    updateDate,
    author.map(o => o.customHashCode),
    publisher,
    rights,
    geospatialExtent,
    contentDescription.map(o => o.customHashCode),
    preview.map(o => o.customHashCode),
    contentByRef.map(o => o.customHashCode),
    offering.map(o => o.customHashCode),
    active,
    resourceMetadata.map(o => o.customHashCode),
    keyword.map(o => o.customHashCode),
    minScaleDenominator,
    maxScaleDenominator,
    folder).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcResource =>
      this.customHashCode.equals(that.customHashCode)
    case _ => false
  }
}

object OwcResource extends LazyLogging {

  def newOf(owcResource: OwcResource, newId: Option[URL] = None): OwcResource =
    owcResource.copy(id = newId.getOrElse(new URL(owcResource.id.toString + "_copy_" + UUID.randomUUID().toString)),
      author = owcResource.author.map(o => o.newOf),
      contentDescription = owcResource.contentDescription.map(o => o.newOf),
      preview = owcResource.preview.map(o => o.newOf),
      contentByRef = owcResource.contentByRef.map(o => o.newOf),
      offering = owcResource.offering.map(o => o.newOf),
      resourceMetadata = owcResource.resourceMetadata.map(o => o.newOf),
      keyword = owcResource.keyword.map(o => o.newOf))

  private val verifyingFeatureTypeReads = new Reads[String] {
    def reads(json: JsValue): JsResult[String] = json.validate[String].flatMap {
      case s if s.equals("Feature") => JsSuccess(s)
      case s => {
        logger.error("JsError ValidationError error.expected.type->Feature")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.type->Feature"))))
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
      ((JsPath \ "properties" \ "authors").read[List[OwcAuthor]] orElse Reads.pure(List[OwcAuthor]())) and
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
      ((JsPath \ "properties" \ "categories").read[List[OwcCategory]] orElse Reads.pure(List[OwcCategory]())) and
      (JsPath \ "properties" \ "minscaledenominator").readNullable[Double](min[Double](0)) and
      (JsPath \ "properties" \ "maxscaledenominator").readNullable[Double](min[Double](0)) and
      (JsPath \ "properties" \ "folder").readNullable[String](minLength[String](1))
    ) (OwcResource.apply _)

  private val owc100validatedResourceReads: Reads[OwcResource] = readFeatureTypeJsonTransform andKeep owc100ResourceReads

  private val owc100ResourceWrites: Writes[OwcResource] = (
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

  private val addFeatureTypeJsonTransform = JsPath.read[JsObject].map(o => Json.obj("type" -> "Feature") ++ o)

  // write the "type": "Feature" without using a field in the case class
  private val owc100ResourceWritesWithFeatureType = Writes[OwcResource] { owc =>
    val js = Json.toJson[OwcResource](owc)(owc100ResourceWrites)
    val result = js.transform(addFeatureTypeJsonTransform).getOrElse(js)
    if (result.equals(js)) {
      logger.error("Couldn't write 'type' -> 'Feature' into Resource GeoJSON")
    } else {
      logger.debug("OwResource toJson adding type->Feature")
    }
    result
  }

  implicit val owc100ResourceFormat: Format[OwcResource] =
    Format(owc100validatedResourceReads, owc100ResourceWritesWithFeatureType)

}
