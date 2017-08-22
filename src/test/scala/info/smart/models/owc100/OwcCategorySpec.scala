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

import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._


class OwcCategorySpec extends WordSpec with MustMatchers with LazyLogging {

  val jsCategory1 =
    """{
      |"term": "groundwater"
      |}""".stripMargin

  val jsCategory1_1 =
    """{
      |"term": ""
      |}""".stripMargin

  val jsCategory2 =
    """{
      |"term": "groundwater",
      |"scheme": "nzhs-freshwater-lexicon"
      |}""".stripMargin

  val jsCategory2_1 =
    """{
      |"term": "groundwater",
      |"scheme": ""
      |}""".stripMargin

  val jsCategory3 =
    """{
      |"term": "groundwater",
      |"scheme": "nzhs-freshwater-lexicon",
      |"label": "Water underground in the pores of the rocks"
      |}""".stripMargin

  val jsCategory3_1 =
    """{
      |"term": "groundwater",
      |"scheme": "nzhs-freshwater-lexicon",
      |"label": ""
      |}""".stripMargin

  val jsCategory4 =
    """{
      |"term": "groundwater",
      |"scheme": "nzhs-freshwater-lexicon",
      |"label": "Water underground in the pores of the rocks",
      |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
      |}""".stripMargin

  "GeoJSON Category aka keywords" should {

    "<props>.categories.term SHALL have a keyword related to the context document or resource" in {
      val jsVal = Json.parse(jsCategory1)

      val fromJson: JsResult[OwcCategory] = Json.fromJson[OwcCategory](jsVal)
      fromJson match {
        case JsSuccess(r: OwcCategory, path: JsPath) => println("Term: " + r.term)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcCategory] = jsVal.validate[OwcCategory]
      result match {
        case s: JsSuccess[OwcCategory] => println("Term: " + s.get.term)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsCategory1).validate[OwcCategory].get.term mustEqual "groundwater"
      Json.parse(jsCategory1_1).validate[OwcCategory].isInstanceOf[JsError] mustBe true
      Json.parse(jsCategory2).validate[OwcCategory].get.term mustEqual "groundwater"
      Json.parse(jsCategory3).validate[OwcCategory].get.term mustEqual "groundwater"
      Json.parse(jsCategory4).validate[OwcCategory].get.term mustEqual "groundwater"

    }

    "<props>.categories.scheme MAY have a related code-list that is identified by the scheme attribute (0..1)" in {
      Json.parse(jsCategory1).validate[OwcCategory].get.scheme must not be defined
      Json.parse(jsCategory2).validate[OwcCategory].get.scheme mustEqual Some("nzhs-freshwater-lexicon")
      Json.parse(jsCategory2_1).validate[OwcCategory].isInstanceOf[JsError] mustBe true
      Json.parse(jsCategory3).validate[OwcCategory].get.scheme mustEqual Some("nzhs-freshwater-lexicon")
      Json.parse(jsCategory4).validate[OwcCategory].get.scheme mustEqual Some("nzhs-freshwater-lexicon")
    }

    "<props>.categories.label MAY have a speaking label in relation to the keyword term (0..1)" in {
      Json.parse(jsCategory1).validate[OwcCategory].get.label must not be defined
      Json.parse(jsCategory2).validate[OwcCategory].get.label must not be defined
      Json.parse(jsCategory3).validate[OwcCategory].get.label mustEqual Some("Water underground in the pores of the rocks")
      Json.parse(jsCategory3_1).validate[OwcCategory].isInstanceOf[JsError] mustBe true
      Json.parse(jsCategory4).validate[OwcCategory].get.label mustEqual Some("Water underground in the pores of the rocks")
    }

    "<props>.categories.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <props>.categories.uuid as unique identifier")
      Json.parse(jsCategory1).validate[OwcCategory].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsCategory2).validate[OwcCategory].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsCategory3).validate[OwcCategory].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsCategory4).validate[OwcCategory].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")
    }
  }

  "OwcCategory Writes" should {

    "write OwcCategory GeoJSON" in {

      val res1 = Json.parse(jsCategory1).validate[OwcCategory].get
      val res2 = Json.parse(jsCategory2).validate[OwcCategory].get
      val res3 = Json.parse(jsCategory3).validate[OwcCategory].get
      val res4 = Json.parse(jsCategory4).validate[OwcCategory].get


      res1.toJson.validate[OwcCategory].get mustEqual res1
      res2.toJson.validate[OwcCategory].get mustEqual res2
      res3.toJson.validate[OwcCategory].get mustEqual res3
      res4.toJson.validate[OwcCategory].get mustEqual res4
    }
  }

  "OwcCategory Custom" should {

    "Copy and Compare" in {

      val res1 = Json.parse(jsCategory1).validate[OwcCategory].get
      val resClone = res1.newOf

      resClone must not equal res1
      resClone.sameAs(res1) mustBe true

      val resClone2 = OwcCategory.newOf(res1)

      resClone2 must not equal res1
      resClone2.sameAs(res1) mustBe true
    }
  }
}
