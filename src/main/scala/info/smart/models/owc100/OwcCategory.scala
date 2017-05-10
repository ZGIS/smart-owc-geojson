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
  * reusable pattern of tagging things in the entry lists for declaration in subsequent processes,
  * e.g. accordeon groups in legends panel(we have to implement that in the mapviewer though)
  *
  * @param uuid
  * @param scheme e.g. for mapviewer: view-groups
  * @param term   identifier of a view group: nz-overview
  * @param label  human readable name of the term: New Zealand Overview, National Scale models..
  */
case class OwcCategory(uuid: UUID, scheme: String, term: String, label: Option[String]) extends LazyLogging {

  /**
    *
    * @return
    */
  def toJson: JsValue = Json.toJson(this)
}

/**
  * companion object for [[OwcCategory]]
  */
object OwcCategory extends LazyLogging {

  implicit val owcCategoryReads: Reads[OwcCategory] = (
    (JsPath \ "scheme").read[String] and
      (JsPath \ "term").read[String] and
      (JsPath \ "label").readNullable[String]
    )(OwcCategoryJs.apply _)

  implicit val owcCategoryWrites: Writes[OwcCategory] = (
    (JsPath \ "scheme").write[String] and
      (JsPath \ "term").write[String] and
      (JsPath \ "label").writeNullable[String]
    )(unlift(OwcCategoryJs.unapply))

  implicit val owcCategoryFormat: Format[OwcCategory] =
    Format(owcCategoryReads, owcCategoryWrites)

}

/**
  * OwcCategory Json stuff
  */
object OwcCategoryJs extends LazyLogging {

  /**
    *
    * @param scheme
    * @param term
    * @param label
    * @return
    */
  def apply(scheme: String, term: String, label: Option[String]): OwcCategory = {
    // Todo, we might find a way to find an existing UUID from DB if entry exists
    val uuid = UUID.randomUUID()
    OwcCategory(uuid, scheme, term, label)
  }

  /**
    *
    * @param arg
    * @return
    */
  def unapply(arg: OwcCategory): Option[(String, String, Option[String])] = {
    Some(arg.scheme, arg.term, arg.label)
  }
}

