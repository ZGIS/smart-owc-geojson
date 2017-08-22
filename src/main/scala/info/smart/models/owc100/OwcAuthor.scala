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
import play.api.libs.json.Reads.minLength
import play.api.libs.json._

import uk.gov.hmrc.emailaddress.{ EmailAddress, PlayJsonFormats}

case class OwcAuthor(
                      name: Option[String],
                      email: Option[EmailAddress] = None,
                      uri: Option[URL] = None,
                      uuid: UUID = UUID.randomUUID()
                    ) extends CustomCopyCompare with LazyLogging {

  def toJson: JsValue = Json.toJson(this)

  def newOf: OwcAuthor = this.copy(uuid = UUID.randomUUID())

  def customHashCode: Int = (name, email, uri).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcAuthor =>
      this.customHashCode.equals(that.customHashCode)
    case _ => false
  }
}

/**
  * companion object for [[OwcAuthor]]
  */
object OwcAuthor extends LazyLogging {

  def newOf(owcAuthor: OwcAuthor): OwcAuthor = owcAuthor.copy(uuid = UUID.randomUUID())

  implicit val owcAuthorReads: Reads[OwcAuthor] = (
    (JsPath \ "name").readNullable[String](minLength[String](1)) and
      (JsPath \ "email").readNullable[EmailAddress](PlayJsonFormats.emailAddressReads) and
      (JsPath \ "uri").readNullable[URL](new UrlFormat) and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()) )
    )(OwcAuthor.apply _)

  implicit val owcAuthorWrites: Writes[OwcAuthor] = (
    (JsPath \ "name").writeNullable[String] and
      (JsPath \ "email").writeNullable[EmailAddress](PlayJsonFormats.emailAddressWrites) and
      (JsPath \ "uri").writeNullable[URL](new UrlFormat) and
      (JsPath \ "uuid").write[UUID]
    )(unlift(OwcAuthor.unapply))

  implicit val owcAuthorFormat: Format[OwcAuthor] = Format(owcAuthorReads, owcAuthorWrites)
}
