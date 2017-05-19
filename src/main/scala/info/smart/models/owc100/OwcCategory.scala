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
import play.api.libs.json.Reads.{minLength, _}
import play.api.libs.json._

case class OwcCategory(
                        term: String,
                        scheme: Option[String],
                        label: Option[String],
                        uuid: UUID = UUID.randomUUID()
                      ) extends LazyLogging {

  def toJson: JsValue = Json.toJson(this)
}

/**
  * companion object for [[OwcCategory]]
  */
object OwcCategory extends LazyLogging {

  implicit val owc100CategoryReads: Reads[OwcCategory] = (
    (JsPath \ "term").read[String](minLength[String](1)) and
      (JsPath \ "scheme").readNullable[String](minLength[String](1)) and
      (JsPath \ "label").readNullable[String](minLength[String](1)) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcCategory.apply _)

  implicit val owc100CategoryWrites: Writes[OwcCategory] = (
    (JsPath \ "term").write[String] and
      (JsPath \ "scheme").writeNullable[String] and
      (JsPath \ "label").writeNullable[String] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcCategory.unapply))

  implicit val owc100CategoryFormat: Format[OwcCategory] = Format(owc100CategoryReads, owc100CategoryWrites)

}


