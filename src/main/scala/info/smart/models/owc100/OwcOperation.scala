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
import play.api.libs.json._

/**
  * + code :CharacterString
  * + method :CharacterString
  * + type :CharacterString
  * + requestURL :URI
  * + request :Content [0..1]
  * + result :Any [0..1]
  * + extension :Any [0..*]
  */
case class OwcOperation(
                         code: String, // operation, i.e. GetCapabilities
                         method: String, // HTTP verb
                         mimeType: Option[String], // MIME media type of the expected results
                         requestUrl: URL,
                         request: Option[OwcContent],
                         result: Option[OwcContent],
                         uuid: UUID = UUID.randomUUID()
                       ) extends CustomCopyCompare with LazyLogging {

  def toJson: JsValue = Json.toJson(this)(OwcOperation.owc100OperationFormat)

  def newOf: OwcOperation = this.copy(uuid = UUID.randomUUID())

  def customHashCode: Int = (code, method, mimeType, requestUrl).##

  def sameAs(o: Any): Boolean = o match {
    case that: OwcOperation => {
      val hash = this.customHashCode.equals(that.customHashCode)
      val request = this.request match {
        case tc: Option[OwcContent] if tc.isDefined => that.request.exists(ac => tc.get.sameAs(ac))
        case tc: Option[OwcContent] if tc.isEmpty => that.request.isEmpty
        case _ => false
      }
      val result = this.result match {
        case tc: Option[OwcContent] if tc.isDefined => that.result.exists(ac => tc.get.sameAs(ac))
        case tc: Option[OwcContent] if tc.isEmpty => that.result.isEmpty
        case _ => false
      }

      hash && request && result
    }
    case _ => false
  }
}

object OwcOperation extends LazyLogging {

  val verifyingHttpMethodsReads = new Reads[String] {
    def reads(json: JsValue): JsResult[String] = {
      val allowedVerbs = List("GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "CONNECT")
      json match {
        case JsString(verb) => {
          if (allowedVerbs.contains(verb.toUpperCase)) {
            JsSuccess(verb.toUpperCase)
          } else {
            logger.error("JsError ValidationError error.expected.validhttpverb")
            JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.validhttpverb"))))
          }
        }
        case _ => {
          logger.error("JsError ValidationError error.expected.jsstring")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
        }
      }
    }
  }

  private val owc100OperationReads: Reads[OwcOperation] = (
    (JsPath \ "code").read[String](minLength[String](1)) and
      (JsPath \ "method").read[String](minLength[String](1) andKeep verifyingHttpMethodsReads) and
      (JsPath \ "type").readNullable[String](minLength[String](1) andKeep new MimeTypeFormat) and
      (JsPath \ "href").read[URL](new UrlFormat) and
      (JsPath \ "request").readNullable[OwcContent] and
      (JsPath \ "result").readNullable[OwcContent] and
      ((JsPath \ "uuid").read[UUID] orElse Reads.pure(UUID.randomUUID()))
    ) (OwcOperation.apply _)

  private val owc100OperationWrites: Writes[OwcOperation] = (
    (JsPath \ "code").write[String] and
      (JsPath \ "method").write[String] and
      (JsPath \ "type").writeNullable[String] and
      (JsPath \ "href").write[URL](new UrlFormat) and
      (JsPath \ "request").writeNullable[OwcContent] and
      (JsPath \ "result").writeNullable[OwcContent] and
      (JsPath \ "uuid").write[UUID]
    ) (unlift(OwcOperation.unapply))

  implicit val owc100OperationFormat: Format[OwcOperation] = Format(owc100OperationReads, owc100OperationWrites)
}
