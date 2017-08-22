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
  * + name :CharacterString
  * + title :CharacterString
  * + abstract :CharacterString [0..1]
  * + default :Boolean [0..1]
  * + legendURL :URI [0..*]
  * + content :Content [0..1]
  * + extension :Any [0..*]
  */
case class OwcStyleSet(
                        name: String,
                        title: String,
                        abstrakt: Option[String] = None,
                        default: Option[Boolean] = None,
                        legendUrl: Option[URL] = None,
                        content: Option[OwcContent] = None,
                        uuid: UUID = UUID.randomUUID()
                      ) extends CustomCopyCompare with LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcStyleSet.owc100StyleSetFormat)

  def newOf: OwcStyleSet = this.copy(uuid = UUID.randomUUID(), content = this.content.map(o => o.newOf))

  def customHashCode: Int = (name, title, abstrakt, default, legendUrl).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcStyleSet => {
      val hash = this.customHashCode.equals(that.customHashCode)
      val content = this.content match {
        case tc: Option[OwcContent] if tc.isDefined => that.content.exists(ac => tc.get.sameAs(ac))
        case tc: Option[OwcContent] if tc.isEmpty => that.content.isEmpty
        case _ => false
      }

      hash && content
    }
    case _ => false
  }
}

object OwcStyleSet extends LazyLogging {

  def newOf(owcStyleSet: OwcStyleSet): OwcStyleSet = owcStyleSet.copy(uuid = UUID.randomUUID(), content = owcStyleSet.content.map(o => o.newOf))

  private val owc100StyleSetReads: Reads[OwcStyleSet] = (
    (JsPath \ "name").read[String](minLength[String](1)) and
      (JsPath \ "title").read[String](minLength[String](1)) and
      (JsPath \ "abstract").readNullable[String](minLength[String](1)) and
      (JsPath \ "default").readNullable[Boolean] and
      (JsPath \ "legendURL").readNullable[URL](new UrlFormat) and
      (JsPath \ "content").readNullable[OwcContent] and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcStyleSet.apply _)

  private val owc100StyleSetWrites: Writes[OwcStyleSet] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "title").write[String] and
      (JsPath \ "abstract").writeNullable[String] and
      (JsPath \ "default").writeNullable[Boolean] and
      (JsPath \ "legendURL").writeNullable[URL](new UrlFormat) and
      (JsPath \ "content").writeNullable[OwcContent] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcStyleSet.unapply))

  implicit val owc100StyleSetFormat: Format[OwcStyleSet] = Format(owc100StyleSetReads, owc100StyleSetWrites)
}
