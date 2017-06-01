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

class OwcContentSpec extends WordSpec with MustMatchers with LazyLogging{

  val xmlContent =
    """<my_srf:RoadCollection gml:id="ID_ROADS1" xsi:schemaLocation="http://www.opengis.net/gml/3.2
      | http://schemas.opengis.net/gml/3.2.1/gml.xsd http://www.opengis.net/owc/1.0/examples/gml/1 road.xsd"
      | xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml/3.2"
      | xmlns:my_srf="http://www.opengis.net/owc/1.0/examples/example1">
      | <my_srf:road><my_srf:Road gml:id="ID_ROAD1">
      | <my_srf:position><gml:LineString gml:id="ID_LINEROAD1">300 200</gml:pos><gml:pos>350 222</gml:pos>
      | </gml:LineString></my_srf:position>
      | <my_srf:width>4.1</my_srf:width><my_srf:name>M30</my_srf:name></my_srf:Road></my_srf:road>
      |</my_srf:RoadCollection>""".stripMargin

  val inlineContent = Json.stringify(JsString(xmlContent))

  val jsContent1 =
    """{"type": "application/gml+xml",
      |"href": "http://www.gns.cri.nz"
      |}
    """.stripMargin

  val jsContent1_1 =
    """{"type": "",
      |"href": "http://www.gns.cri.nz"
      |}
    """.stripMargin

  val jsContent2 =
    s"""{"type" : "application/gml+xml",
       |"content" : ${inlineContent}
       |}
    """.stripMargin

  val jsContent2_1 =
    """{"type" : "application/gml+xml",
      |"content" : ""
      |}
    """.stripMargin

  val jsContent3 =
    """{"type": "application/gml+xml",
      |"href": "http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection",
      |"title": "ID_ROADS1:M30"
      |}
    """.stripMargin

  val jsContent3_1 =
    s"""{"type": "application/gml+xml",
       |"content" : ${inlineContent},
       |"title": "ID_ROADS1:M30"
       |}
      """.stripMargin

  val jsContent3_2 =
    """{"type": "application/gml+xml",
      |"href": "http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection",
      |"title": ""
      |}
    """.stripMargin

  val jsContent4 =
    """{"type": "application/gml+xml",
      |"href": "http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection",
      |"title": "ID_ROADS1:M30",
      |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
      |}
    """.stripMargin

  val jsContent4_1 =
    s"""{"type": "application/gml+xml",
       |"href": "http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection",
       |"title": "ID_ROADS1:M30",
       |"content": ${inlineContent},
       |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
       |}
      """.stripMargin

  val jsContent4_2 =
    s"""{"type": "application/gml+xml",
       |"title": "ID_ROADS1:M30",
       |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
       |}
      """.stripMargin

  val jsContent4_3 =
    s"""{"type": "not-good-mimetype",
       |"title": "ID_ROADS1:M30",
       |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
       |}
      """.stripMargin

  "DataType OWC:Content GeoJSON Section 7.1.5" should {

    "<cont>.type SHALL have MIME type of the Content" in {
      val jsVal = Json.parse(jsContent4_1)

      val fromJson: JsResult[OwcContent] = Json.fromJson[OwcContent](jsVal)
      fromJson match {
        case JsSuccess(r: OwcContent, path: JsPath) => println("url: " + r.url)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcContent] = jsVal.validate[OwcContent]
      result match {
        case s: JsSuccess[OwcContent] => println("content: " + s.get.content)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsContent1).validate[OwcContent].get.mimeType mustEqual "application/gml+xml"
      Json.parse(jsContent1_1).validate[OwcContent].isInstanceOf[JsError] mustBe true
      Json.parse(jsContent2).validate[OwcContent].get.mimeType mustEqual "application/gml+xml"
      Json.parse(jsContent3).validate[OwcContent].get.mimeType mustEqual "application/gml+xml"
      Json.parse(jsContent3_1).validate[OwcContent].get.mimeType mustEqual "application/gml+xml"
      Json.parse(jsContent4).validate[OwcContent].get.mimeType mustEqual "application/gml+xml"
      Json.parse(jsContent4_3).validate[OwcContent].isInstanceOf[JsError] mustBe true
    }

    "<cont>.href MAY have URL of the Content (0..1)" in {
      Json.parse(jsContent1).validate[OwcContent].get.url mustEqual Some(new java.net.URL("http://www.gns.cri.nz"))
      Json.parse(jsContent2).validate[OwcContent].get.url mustEqual None
      Json.parse(jsContent3).validate[OwcContent].get.url mustEqual Some(new java.net.URL("http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection"))
      Json.parse(jsContent3_1).validate[OwcContent].get.url mustEqual None
      Json.parse(jsContent4).validate[OwcContent].get.url mustEqual Some(new java.net.URL("http://data.roads.wherever.com/wfs?service=WFS&request=GetFeature&typename=my_srf:RoadCollection"))
    }

    "<cont>.content MAY have In-line content for the Content element that can contain any text encoded media type (0..1)" in {
      Json.parse(jsContent1).validate[OwcContent].get.content mustEqual None
      Json.parse(jsContent2).validate[OwcContent].get.content mustEqual Some(xmlContent)
      Json.parse(jsContent2_1).validate[OwcContent].isInstanceOf[JsError] mustBe true
      Json.parse(jsContent3).validate[OwcContent].get.content mustEqual None
      Json.parse(jsContent3_1).validate[OwcContent].get.content mustEqual Some(xmlContent)
      Json.parse(jsContent4).validate[OwcContent].get.content mustEqual None

      logger.info("If the 'href' attribute is present, the element content SHALL be empty. If 'href' is not provided, content SHALL be provided.")
      Json.parse(jsContent4_1).validate[OwcContent].isInstanceOf[JsError] mustBe true
      Json.parse(jsContent4_2).validate[OwcContent].isInstanceOf[JsError] mustBe true

    }

    "<cont>.title MAY have Title of the Content (0..1)" in {
      Json.parse(jsContent1).validate[OwcContent].get.title mustEqual None
      Json.parse(jsContent2).validate[OwcContent].get.title mustEqual None
      Json.parse(jsContent3).validate[OwcContent].get.title mustEqual Some("ID_ROADS1:M30")
      Json.parse(jsContent3_1).validate[OwcContent].get.title mustEqual Some("ID_ROADS1:M30")
      Json.parse(jsContent3_2).validate[OwcContent].isInstanceOf[JsError] mustBe true
      Json.parse(jsContent4).validate[OwcContent].get.title mustEqual Some("ID_ROADS1:M30")
    }

    "<cont>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <cont>.uuid as unique identifier")
      Json.parse(jsContent1).validate[OwcContent].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsContent2).validate[OwcContent].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsContent3).validate[OwcContent].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsContent3_1).validate[OwcContent].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsContent4).validate[OwcContent].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")
    }
  }

  "OwcContent Writes" should {

    "write OwcContent GeoJSON" in {

      val res1 = Json.parse(jsContent1).validate[OwcContent].get
      val res2 = Json.parse(jsContent2).validate[OwcContent].get
      val res3 = Json.parse(jsContent3).validate[OwcContent].get
      val res4 = Json.parse(jsContent4).validate[OwcContent].get

      res1.toJson.validate[OwcContent].get mustEqual res1
      res2.toJson.validate[OwcContent].get mustEqual res2
      res3.toJson.validate[OwcContent].get mustEqual res3
      res4.toJson.validate[OwcContent].get mustEqual res4
    }
  }
}
