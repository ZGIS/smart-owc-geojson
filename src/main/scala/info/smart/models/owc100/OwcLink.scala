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

import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.json._

/**
  *
  * @param uuid
  * @param rel one of typically "self", "profile", "icon", "via"
  * @param mimeType
  * @param href
  * @param title
  */
case class OwcLink(uuid: UUID, rel: String, mimeType: Option[String], href: String, title: Option[String]) extends LazyLogging {

  /**
    *
    * @return
    */
  def toJson: JsValue = Json.toJson(this)
}

/**
  * companion object for [[OwcLink]]
  */
object OwcLink extends LazyLogging {

  implicit val owcLinkReads: Reads[OwcLink] = (
    (JsPath \ "rel").read[String] and
      (JsPath \ "type").readNullable[String] and
      (JsPath \ "href").read[String] and
      (JsPath \ "title").readNullable[String]
    )(OwcLinkJs.apply _)

  implicit val owcLinkWrites: Writes[OwcLink] = (
    (JsPath \ "rel").write[String] and
      (JsPath \ "type").writeNullable[String] and
      (JsPath \ "href").write[String] and
      (JsPath \ "title").writeNullable[String]
    )(unlift(OwcLinkJs.unapply))

  implicit val owcLinkFormat: Format[OwcLink] =
    Format(owcLinkReads, owcLinkWrites)

}

/**
  * OwcLink Json stuff
  */
object OwcLinkJs extends LazyLogging {

  /**
    *
    * @param rel
    * @param mimeType
    * @param href
    * @param title
    * @return
    */
  def apply(rel: String, mimeType: Option[String], href: String, title: Option[String]): OwcLink = {
    // Todo, we might find a way to find an existing UUID from DB if entry exists
    val uuid = UUID.randomUUID()
    OwcLink(uuid, rel, mimeType, href, title)
  }

  /**
    *
    * @param arg
    * @return
    */
  def unapply(arg: OwcLink): Option[(String, Option[String], String, Option[String])] = {
    Some(arg.rel, arg.mimeType, arg.href, arg.title)
  }
}
