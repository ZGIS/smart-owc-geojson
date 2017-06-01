/*
 * Copyright (C) 2011-2017 Interfaculty Department of Geoinformatics, University of
 * Salzburg (Z_GIS) & Institute of Geological and Nuclear Sciences Limited (GNS Science)
 * in the SMART Aquifer Characterisation (SAC) programme funded by the New Zealand
 * Ministry of Business, Innovation and Employment (MBIE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._
import uk.gov.hmrc.emailaddress.EmailAddress


class OwcAuthorSpec extends WordSpec with MustMatchers with LazyLogging {

  val jsAuthor1 =
    """{
      |"name": "John Doe"
      |}""".stripMargin

  val jsAuthor1_1 =
    """{
      |}""".stripMargin

  val jsAuthor1_2 =
    """{
      |"name": ""
      |}""".stripMargin

  val jsAuthor2 =
    """{
      |"name" : "Joe Doe",
      |"email" : "jdoe@some.com"
      |}""".stripMargin

  val jsAuthor2_1 =
    """{
      |"name" : "Joe Doe",
      |"email" : "jdoe-some.com"
      |}""".stripMargin

  val jsAuthor3 =
    """{
      |"name" : "Joe Doe",
      |"email" : "jdoe@some.com",
      |"uri" : "http://some.com/jdoe"
      |}""".stripMargin

  val jsAuthor3_1 =
    """{
      |"name" : "Joe Doe",
      |"email" : "jdoe@some.com",
      |"uri" : "httpxx://some.com/jdoe"
      |}""".stripMargin

  val jsAuthor4 =
    """{
      |"name" : "Joe Doe",
      |"email" : "jdoe@some.com",
      |"uri" : "http://some.com/jdoe",
      |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
      |}""".stripMargin

  "GeoJSON Authors " should {

    "<props>.authors.name It MAY contain name (conveys a human-readable name for the person)" in {
      val jsVal = Json.parse(jsAuthor1_1)

      val fromJson: JsResult[OwcAuthor] = Json.fromJson[OwcAuthor](jsVal)
      fromJson match {
        case JsSuccess(r: OwcAuthor, path: JsPath) => println("name: " + r.name)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcAuthor] = jsVal.validate[OwcAuthor]
      result match {
        case s: JsSuccess[OwcAuthor] => println("email: " + s.get.email)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsAuthor1).validate[OwcAuthor].get.name mustEqual Some("John Doe")
      Json.parse(jsAuthor1_1).validate[OwcAuthor].get.isInstanceOf[OwcAuthor] mustBe true
      Json.parse(jsAuthor1_1).validate[OwcAuthor].get.name must not be defined
      Json.parse(jsAuthor1_2).validate[OwcAuthor].isInstanceOf[JsError] mustBe true
    }

    "<props>.authors.email MAY have email (email address for the person) (0..1)" in {
      Json.parse(jsAuthor1).validate[OwcAuthor].get.email must not be defined
      Json.parse(jsAuthor2).validate[OwcAuthor].get.email mustEqual Some(EmailAddress("jdoe@some.com"))
      Json.parse(jsAuthor2_1).validate[OwcAuthor].isInstanceOf[JsError] mustBe true
    }

    "<props>.authors.uri MAY have a uri (home page for the person) (0..1)" in {
      Json.parse(jsAuthor1).validate[OwcAuthor].get.uri must not be defined
      Json.parse(jsAuthor2).validate[OwcAuthor].get.uri must not be defined
      Json.parse(jsAuthor3).validate[OwcAuthor].get.uri mustEqual Some(new URL("http://some.com/jdoe"))
      Json.parse(jsAuthor3_1).validate[OwcAuthor].isInstanceOf[JsError] mustBe true
    }

    "<props>.authors.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <props>.authors.uuid as unique identifier")
      Json.parse(jsAuthor1).validate[OwcAuthor].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsAuthor2).validate[OwcAuthor].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsAuthor3).validate[OwcAuthor].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsAuthor4).validate[OwcAuthor].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")
    }
  }

  "OwcAuthor Writes" should {

    "write OwcAuthor GeoJSON" in {

      val res1 = Json.parse(jsAuthor1).validate[OwcAuthor].get
      val res2 = Json.parse(jsAuthor2).validate[OwcAuthor].get
      val res3 = Json.parse(jsAuthor3).validate[OwcAuthor].get
      val res4 = Json.parse(jsAuthor4).validate[OwcAuthor].get

      res1.toJson.validate[OwcAuthor].get mustEqual res1
      res2.toJson.validate[OwcAuthor].get mustEqual res2
      res3.toJson.validate[OwcAuthor].get mustEqual res3
      res4.toJson.validate[OwcAuthor].get mustEqual res4
    }
  }
}
