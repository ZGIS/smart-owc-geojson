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
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.json._

/**
  * OwcLink in most cases will have an array of links under the path of the rel
  *
  * OWC:Context specReference attribute: atom rel="profile" - geojson links.profiles[] array
  * OWC:Context contextMetadata attribute: atom rel="via" - geojson links.via[] array
  *
  * OWC:Resource contentDescription attribute: atom rel="alternate" - geojson links.alternates[] array
  * OWC:Resource preview attribute: atom rel="icon" - geojson links.previews[] array
  * OWC:Resource contentByRef attribute: atom rel="enclosure" - geojson links.data[] array
  * OWC:Resource resourceMetadata attribute: atom rel="via" - geojson links.via[] array
  *
  * links for data and previews (aka rels enclosure and icon should have length attributes set)
  *
  * + href :URI
  * + type :String [0..1]
  * + lang :String [0..1]
  * + title :String [0..1]
  * + length :Integer [0..1]
  * + extension :Any [0..*]
  */
case class OwcLink(
                    href: URL,
                    mimeType: Option[String],
                    lang: Option[String],
                    title: Option[String],
                    length: Option[Int],
                    rel: String, // can at least stay here via extension :-)
                    uuid: UUID = UUID.randomUUID()
                  ) extends LazyLogging {
  def toJson: JsValue = Json.toJson(this)
}

object OwcLink extends LazyLogging {

  val verifyingKnownRelationsReads = new Reads[String] {
    def reads(json: JsValue): JsResult[String] = {
      val knownRelations = List("profile", "via", "alternate", "icon", "enclosure")
      json match {
        case JsString(rel) => {
          if (knownRelations.contains(rel.toLowerCase())) {
            JsSuccess(rel.toLowerCase)
          } else {
            logger.error("JsError ValidationError error.expected.validrel")
            JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.validrel"))))
          }
        }
        case _ => {
          logger.error("JsError ValidationError error.expected.jsstring")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
        }
      }
    }
  }

  implicit val owc100LinkReads: Reads[OwcLink] = (
    (JsPath \ "href").read[URL](new UrlFormat) and
      (JsPath \ "type").readNullable[String](minLength[String](1) andKeep new MimeTypeFormat) and
      (JsPath \ "lang").readNullable[String](minLength[String](1) andKeep new IsoLangFormat) and
      (JsPath \ "title").readNullable[String](minLength[String](1)) and
      (JsPath \ "length").readNullable[Int](min[Int](0)) and
      ((JsPath \ "rel").read[String](verifyingKnownRelationsReads) orElse Reads.pure("alternate") ) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()) )
    ) (OwcLink.apply _)

  implicit val owc100LinkWrites: Writes[OwcLink] = (
    (JsPath \ "href").write[URL](new UrlFormat) and
      (JsPath \ "type").writeNullable[String] and
      (JsPath \ "lang").writeNullable[String] and
      (JsPath \ "title").writeNullable[String] and
      (JsPath \ "length").writeNullable[Int] and
      (JsPath \ "rel").write[String] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcLink.unapply))

  implicit val owc100OfferingFormat: Format[OwcLink] = Format(owc100LinkReads, owc100LinkWrites)
}
