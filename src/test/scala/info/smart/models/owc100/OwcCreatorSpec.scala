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

class OwcCreatorSpec extends WordSpec with MustMatchers with LazyLogging{

  val jsCreator1 = """{
                     |"title": "Web Enterprise Suite"
                     |}""".stripMargin

  val jsCreator2 = """{
                     |"title": "Web Enterprise Suite",
                     |"uri": "http://www.somewebside.org/WebEnterpriseSuite"
                     |}""".stripMargin

  val jsCreator3 = """{
                     |"title": "Web Enterprise Suite",
                     |"uri": "http://www.somewebside.org/WebEnterpriseSuite",
                     |"version": "1.0.0"
                     |}""".stripMargin

  val jsCreator4 = """{
                     |"title": "Web Enterprise Suite",
                     |"uri": "http://www.somewebside.org/WebEnterpriseSuite",
                     |"version": "1.0.0",
                     |"uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                     |}""".stripMargin

  val jsCreator2_1 = """{
                       |"title": "Web Enterprise Suite",
                       |"uri": "xxx://www.somewebside.org/WebEnterpriseSuite",
                       |"version": "1.0.0",
                       |"uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                       |}""".stripMargin

  val jsCreator2_2 = """{
                       |"version": "1.0.0",
                       |"uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                       |}""".stripMargin

  "DataType OWC:CreatorApplication GeoJSON Section 7.1.8" should {

    "<xz>.properties.generator.title MAY have Title or name of the application (0..1)" in {
      Json.parse(jsCreator1).validate[OwcCreatorApplication].get.title mustEqual Some("Web Enterprise Suite")
      Json.parse(jsCreator2_2).validate[OwcCreatorApplication].get.title mustEqual None
    }

    "<xz>.properties.generator.uri MAY have URI describing the creator application, that is relevant to the client (web address). (0..1)" in {
      Json.parse(jsCreator1).validate[OwcCreatorApplication].get.uri mustEqual None
      Json.parse(jsCreator2).validate[OwcCreatorApplication].get.uri mustEqual Some(new java.net.URL("http://www.somewebside.org/WebEnterpriseSuite"))
      Json.parse(jsCreator2_1).validate[OwcCreatorApplication].isInstanceOf[JsError] mustBe true
    }

    "<xz>.properties.generator. version MAY have Version of the creator application (0..1)" in {
      Json.parse(jsCreator1).validate[OwcCreatorApplication].get.version mustEqual None
      Json.parse(jsCreator3).validate[OwcCreatorApplication].get.version mustEqual Some("1.0.0")
      Json.parse(jsCreator4).validate[OwcCreatorApplication].get.uuid mustEqual UUID.fromString("a12c7aeb-a822-49d7-8a66-e77fa713724f")
      Json.parse(jsCreator1).validate[OwcCreatorApplication].get.uuid.isInstanceOf[UUID] mustBe true
    }
  }

  "OwcCreatorApplication Writes" should {

    "write OwcCreatorApplication GeoJSON" in {

      val res1 = Json.parse(jsCreator1).validate[OwcCreatorApplication].get
      val res2 = Json.parse(jsCreator2).validate[OwcCreatorApplication].get
      val res3 = Json.parse(jsCreator3).validate[OwcCreatorApplication].get
      val res4 = Json.parse(jsCreator4).validate[OwcCreatorApplication].get


      res1.toJson.validate[OwcCreatorApplication].get mustEqual res1
      res2.toJson.validate[OwcCreatorApplication].get mustEqual res2
      res3.toJson.validate[OwcCreatorApplication].get mustEqual res3
      res4.toJson.validate[OwcCreatorApplication].get mustEqual res4
    }
  }

  val jsDisplay1 = """{
                     |"pixelWidth": 600,
                     |"pixelHeight": 400,
                     |"mmPerPixel": 0.28
                     |}""".stripMargin

  val jsDisplay1_1 = """{
                       |"pixelWidth": -600,
                       |"pixelHeight": 400,
                       |"mmPerPixel": 0.28
                       |}""".stripMargin

  val jsDisplay1_2 = """{
                       |"pixelWidth": 600,
                       |"pixelHeight": 400,
                       |"mmPerPixel": 28
                       |}""".stripMargin

  val jsDisplay1_3 = """{
                       |"pixelWidth": "600",
                       |"pixelHeight": 400,
                       |"mmPerPixel": 0.28
                       |}""".stripMargin

  // apparently casts such notations accordingly :-p
  val jsDisplay1_4 = """{
                       |"pixelWidth": 600,
                       |"pixelHeight": 400,
                       |"mmPerPixel": 2.235E+02
                       |}""".stripMargin

  val jsDisplay1_5 = """{
                       |"pixelWidth": 600,
                       |"pixelHeight": -400,
                       |"mmPerPixel": 0.28
                       |}""".stripMargin

  val jsDisplay1_6 = """{
                       |"pixelWidth": 600,
                       |"pixelHeight": -400,
                       |"mmPerPixel": -0.28
                       |}""".stripMargin

  val jsDisplay2 = """{
                     |"pixelWidth": 600,
                     |"pixelHeight": 400,
                     |"mmPerPixel": 0.28,
                     |"uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                     |}""".stripMargin

  // uuid too short, will fail and then "orElse" creates new UUID on the fly
  val jsDisplay2_1 = """{
                       |"pixelWidth": 600,
                       |"pixelHeight": 400,
                       |"mmPerPixel": 0.28,
                       |"uuid": "a12c7aeb-a822-8a66-e77fa713724f"
                       |}""".stripMargin

  "DataType OWC:CreatorDisplay GeoJSON Section 7.1.9" should {

    "<xz>.properties.display.pixelWidth MAY have Width measured in pixels of the display showing the Area of Interest, Positive Integer (0..1)" in {
      val jsVal = Json.parse(jsDisplay2_1)

      val fromJson: JsResult[OwcCreatorDisplay] = Json.fromJson[OwcCreatorDisplay](jsVal)
      fromJson match {
        case JsSuccess(r: OwcCreatorDisplay, path: JsPath) => println("uuid: " + r.uuid.toString)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcCreatorDisplay] = jsVal.validate[OwcCreatorDisplay]
      result match {
        case s: JsSuccess[OwcCreatorDisplay] => println("mmPerPixel: " + s.get.mmPerPixel)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsDisplay1).validate[OwcCreatorDisplay].get.pixelWidth mustEqual Some(600)
      Json.parse(jsDisplay1_1).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe true
      Json.parse(jsDisplay1_3).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe true

    }

    "<xz>.properties.display.pixelHeight MAY have Width measured in pixels of the display showing the Area of Interest, Positive Integer (0..1)" in {
      Json.parse(jsDisplay1).validate[OwcCreatorDisplay].get.pixelHeight mustEqual Some(400)
      Json.parse(jsDisplay1_1).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe true
      Json.parse(jsDisplay1_5).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe true
    }

    "<xz>.properties.display.mmPerPixel MAY have The size of a pixel of the display in milimeters (to allow for the real display size to be calculated), Double (0..1)" in {
      Json.parse(jsDisplay1).validate[OwcCreatorDisplay].get.mmPerPixel mustEqual Some(0.28)
      Json.parse(jsDisplay1_2).validate[OwcCreatorDisplay].get.mmPerPixel mustEqual Some(28.0)
      Json.parse(jsDisplay1_4).validate[OwcCreatorDisplay].get.mmPerPixel mustEqual Some(223.5)
      Json.parse(jsDisplay1_6).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe true
    }

    "<xz>.properties.display.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <xz>.properties.display.uuid as unique identifier")

      Json.parse(jsDisplay1).validate[OwcCreatorDisplay].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsDisplay2).validate[OwcCreatorDisplay].get.uuid mustEqual UUID.fromString("a12c7aeb-a822-49d7-8a66-e77fa713724f")
      Json.parse(jsDisplay2_1).validate[OwcCreatorDisplay].isInstanceOf[JsError] mustBe false
    }
  }

  "OwcCreatorDisplay Writes" should {

    "write OwcCreatorDisplay GeoJSON" in {

      val res1 = Json.parse(jsDisplay1).validate[OwcCreatorDisplay].get
      val res2 = Json.parse(jsDisplay2).validate[OwcCreatorDisplay].get

      res1.toJson.validate[OwcCreatorDisplay].get mustEqual res1
      res2.toJson.validate[OwcCreatorDisplay].get mustEqual res2
    }
  }
}
