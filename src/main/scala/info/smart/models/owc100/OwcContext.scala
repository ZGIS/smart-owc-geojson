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
import play.api.libs.json._

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
  * +----+ creatorApplication :CreatorApplication [0..1]
  * +----+ creatorDisplay :CreatorDisplay [0..1]
  * + rights :CharacterString [0..1]
  * + areaOfInterest :GM_Envelope [0..1]
  * + timeIntervalOfInterest :TM_GeometricPrimitive [0..1]
  * + keyword :CharacterString [0..*]
  * + extension :Any [0..*]
  */
case class OwcContext(
                       id: URL,
                       areaOfInterest: Option[Rectangle] = None,
                       specReference: List[OwcLink] = List(OwcProfile.CORE.newOf), // aka links.profiles[] and rel=profile
                       contextMetadata: List[OwcLink] = List(), // e.g. links.via[] and rel=via
                       language: String = "en",
                       title: String,
                       subtitle: Option[String] = None,
                       updateDate: OffsetDateTime = OffsetDateTime.now(),
                       author: List[OwcAuthor] = List(),
                       publisher: Option[String] = None,
                       creatorApplication: Option[OwcCreatorApplication] = None,
                       creatorDisplay: Option[OwcCreatorDisplay] = None,
                       rights: Option[String] = None,
                       timeIntervalOfInterest: Option[List[OffsetDateTime]] = None,
                       keyword: List[OwcCategory] = List(),
                       resource: List[OwcResource]
                     ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcContext.owc100ContextFormat)

  def newOf(newId: URL = new URL(this.id.toString + "_copy_" + UUID.randomUUID().toString)): OwcContext =
    this.copy(id = newId,
      specReference = specReference.map(o => o.newOf),
      contextMetadata = contextMetadata.map(o => o.newOf),
      creatorApplication = creatorApplication.map(o => o.newOf),
      creatorDisplay = creatorDisplay.map(o => o.newOf),
      author = author.map(o => o.newOf),
      keyword = keyword.map(o => o.newOf),
      resource = resource.map(o => o.newOf()))

  // we intentially leave id away to allow sameAs function to behave as all other ones, compare contents and ignore (uu)ids
  def customHashCode: Int = (
    areaOfInterest,
    specReference.map(o => o.customHashCode),
    contextMetadata.map(o => o.customHashCode),
    language,
    title,
    subtitle,
    updateDate,
    author.map(o => o.customHashCode),
    publisher,
    creatorApplication.map(o => o.customHashCode),
    creatorDisplay.map(o => o.customHashCode),
    rights,
    timeIntervalOfInterest,
    keyword.map(o => o.customHashCode),
    resource.map(o => o.customHashCode)).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcContext =>
      this.customHashCode.equals(that.customHashCode)
    case _ => false
  }
}

object OwcContext extends LazyLogging {

  def newOf(owcContext: OwcContext, newId: Option[URL] = None): OwcContext =
    owcContext.copy(id = newId.getOrElse(new URL(owcContext.id.toString + "_copy_" + UUID.randomUUID().toString)),
      specReference = owcContext.specReference.map(o => o.newOf),
      contextMetadata = owcContext.contextMetadata.map(o => o.newOf),
      creatorApplication = owcContext.creatorApplication.map(o => o.newOf),
      creatorDisplay = owcContext.creatorDisplay.map(o => o.newOf),
      author = owcContext.author.map(o => o.newOf),
      keyword = owcContext.keyword.map(o => o.newOf),
      resource = owcContext.resource.map(o => o.newOf()))

  private val verifyingFeatureColTypeReads = new Reads[String] {
    def reads(json: JsValue): JsResult[String] = json.validate[String].flatMap {
      case s if s.equals("FeatureCollection") => JsSuccess(s)
      case s => {
        logger.error("JsError ValidationError error.expected.type->FeatureCollection")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.type->FeatureCollection"))))
      }
    }
  }

  // read validate the "type": "FeatureCollection" without using a field in the case class
  private val readFeatureColTypeJsonTransform: Reads[String] = (JsPath \ "type").read[String](verifyingFeatureColTypeReads)

  /**
    * validate if authors are available, either at owcContext level, or in every owcResource
    *
    * @param owcContext
    * @return
    */
  private def validateAuthorsRequirement(owcContext: OwcContext) : Boolean = {
    if (owcContext.author.isEmpty) {
      if (owcContext.resource.filter(res => res.author.length > 0).length < owcContext.resource.length) {
        logger.error("Couldn't validate Authors Requirement, OwcContext.author.isEmpty and not OwcAuthor provided in every OwcResource")
        false
      } else {
        true
      }
    } else {
      true
    }
  }

  private val owc100ContextReads: Reads[OwcContext] = (
    (JsPath \ "id").read[URL](new UrlFormat) and
      (JsPath \ "bbox").readNullable[Rectangle](new BboxArrayFormat) and
      (JsPath \ "properties" \ "links" \ "profiles").read[List[OwcLink]](new OwcProfileListFormat) and
      ((JsPath \ "properties" \ "links" \ "via").read[List[OwcLink]] orElse Reads.pure(List[OwcLink]())) and
      (JsPath \ "properties" \ "lang").read[String](minLength[String](1)) and
      (JsPath \ "properties" \ "title").read[String](minLength[String](1)) and
      (JsPath \ "properties" \ "subtitle").readNullable[String](minLength[String](1)) and
      (JsPath \ "properties" \ "updated").read[OffsetDateTime] and
      ((JsPath \ "properties" \ "authors").read[List[OwcAuthor]] orElse Reads.pure(List[OwcAuthor]())) and
      (JsPath \ "properties" \ "publisher").readNullable[String](minLength[String](1)) and
      (JsPath \ "properties" \ "generator").readNullable[OwcCreatorApplication] and
      (JsPath \ "properties" \ "display").readNullable[OwcCreatorDisplay] and
      (JsPath \ "properties" \ "rights").readNullable[String](minLength[String](1)) and
      (JsPath \ "properties" \ "date").readNullable[List[OffsetDateTime]](new TemporalExtentFormat) and
      ((JsPath \ "properties" \ "categories").read[List[OwcCategory]] orElse Reads.pure(List[OwcCategory]())) and
      ((JsPath \ "features").read[List[OwcResource]])
    ) (OwcContext.apply _).filter { validateAuthorsRequirement }

  // read and validate first Reads[String] and then second Reads[OwcContext and only keep second result
  private val owc100validatedContextReads: Reads[OwcContext] = readFeatureColTypeJsonTransform andKeep owc100ContextReads

  private val owc100ContextWrites: Writes[OwcContext] = (
    (JsPath \ "id").write[URL](new UrlFormat) and
      (JsPath \ "bbox").writeNullable[Rectangle](new BboxArrayFormat) and
      (JsPath \ "properties" \ "links" \ "profiles").write[List[OwcLink]](new OwcProfileListFormat) and
      (JsPath \ "properties" \ "links" \ "via").write[List[OwcLink]] and
      (JsPath \ "properties" \ "lang").write[String] and
      (JsPath \ "properties" \ "title").write[String] and
      (JsPath \ "properties" \ "subtitle").writeNullable[String] and
      (JsPath \ "properties" \ "updated").write[OffsetDateTime] and
      (JsPath \ "properties" \ "authors").write[List[OwcAuthor]] and
      (JsPath \ "properties" \ "publisher").writeNullable[String] and
      (JsPath \ "properties" \ "generator").writeNullable[OwcCreatorApplication] and
      (JsPath \ "properties" \ "display").writeNullable[OwcCreatorDisplay] and
      (JsPath \ "properties" \ "rights").writeNullable[String] and
      (JsPath \ "properties" \ "date").writeNullable[List[OffsetDateTime]](new TemporalExtentFormat) and
      (JsPath \ "properties" \ "categories").write[List[OwcCategory]] and
      (JsPath \ "features").write[List[OwcResource]]
    ) (unlift(OwcContext.unapply))

  private val addFeatureCollectionTypeJsonTransform = JsPath.read[JsObject].map(o =>
    Json.obj("type" -> "FeatureCollection") ++ o
  )

  // write the "type": "FeatureCollection" without using a field in the case class
  private val owc100ContextWritesWithFeatureCollectionType = Writes[OwcContext] { owc =>
    val js = Json.toJson[OwcContext](owc)(owc100ContextWrites)
    val result = js.transform(addFeatureCollectionTypeJsonTransform).getOrElse(js)
    if (result.equals(js)) {
      logger.error("Couldn't write 'type' -> 'FeatureCollection' into Resource GeoJSON")
    } else {
      logger.debug("OwResource toJson adding type->FeatureCollection")
    }
    result
  }

  implicit val owc100ContextFormat: Format[OwcContext] =
    Format(owc100validatedContextReads, owc100ContextWritesWithFeatureCollectionType)


}
