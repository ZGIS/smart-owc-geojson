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
import play.api.libs.json.Reads.{minLength, _}
import play.api.libs.json._

/**
  * + type :CharacterString
  * + URL :URI [0..1]
  * + content :Any [0..1]
  * + extension :Any [0..*]
  */
case class OwcContent(
                       mimeType: String,
                       url: Option[URL],
                       title: Option[String],
                       content: Option[String],
                       uuid: UUID = UUID.randomUUID()
                     ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)
}

object OwcContent extends LazyLogging {

  implicit val owc100ContentReads: Reads[OwcContent] = (
    (JsPath \ "type").read[String](minLength[String](1)) and
      (JsPath \ "href").readNullable[URL](new UrlFormat) and
      (JsPath \ "title").readNullable[String](minLength[String](1)) and
      (JsPath \ "content").readNullable[String](minLength[String](1)) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    )(OwcContent.apply _).filter{ owc =>
    (owc.url.isDefined && owc.content.isEmpty) || (owc.url.isEmpty && owc.content.isDefined)
  }

  implicit val owc100ContentWrites: Writes[OwcContent] = (
    (JsPath \ "type").write[String] and
      (JsPath \ "href").writeNullable[URL](new UrlFormat) and
      (JsPath \ "title").writeNullable[String] and
      (JsPath \ "content").writeNullable[String] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcContent.unapply))

  implicit val owc100ContentFormat: Format[OwcContent] = Format(owc100ContentReads, owc100ContentWrites)
}