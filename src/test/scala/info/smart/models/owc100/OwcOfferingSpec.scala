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

class OwcOfferingSpec extends WordSpec with MustMatchers with LazyLogging {

  val jsOff1 =
    """{
      |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms"
      |}
    """.stripMargin

  val jsOff1_1 =
    """{
      |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/xxx"
      |}
    """.stripMargin

  val jsOff1_2 =
    """{
      |"code" : "httpx://www.opengis.net/spec/owc-geojson/1.0/req/wms"
      |}
    """.stripMargin

  val jsOff2 =
    """{
      |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
      |"operations" : [{
      |  "code" : "GetCapabilities",
      |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
      |  }]
      |}
    """.stripMargin

  val jsOff3 =
    """{
      |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
      |"operations" : [{
      |  "code" : "GetCapabilities",
      |  "method" : "GET",
      |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
      |  }],
      |"uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
      |}
    """.stripMargin

  val sldAbstrakt = "SLD Cook Book: Simple Line extracted from http://docs.geoserver.org/latest/en/user/_downloads/line_simpleline.sld"

  val jsStyle1 = s"""{
                    |"name": "Simple Line",
                    |"title": "SLD Cook Book: Simple Line",
                    |"abstract": "$sldAbstrakt"
                    |}
                   """.stripMargin

  // val inlineStyleSet3 = Json.stringify(Json.parse(jsStyle1))

  val jsOff4 =
    s"""{
       |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
       |"operations" : [{
       |  "code" : "GetCapabilities",
       |  "method" : "GET",
       |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
       |  }],
       |"styles": [ $jsStyle1 ],
       |"uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
       |}
      """.stripMargin

  val xmlContent1 =
    """<my_srf:RoadCollection gml:id="ID_ROADS1" xsi:schemaLocation="http://www.opengis.net/gml/3.2
      | http://schemas.opengis.net/gml/3.2.1/gml.xsd http://www.opengis.net/owc/1.0/examples/gml/1 road.xsd"
      | xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml/3.2"
      | xmlns:my_srf="http://www.opengis.net/owc/1.0/examples/example1">
      | <my_srf:road><my_srf:Road gml:id="ID_ROAD1">
      | <my_srf:position><gml:LineString gml:id="ID_LINEROAD1">300 200</gml:pos><gml:pos>350 222</gml:pos>
      | </gml:LineString></my_srf:position>
      | <my_srf:width>4.1</my_srf:width><my_srf:name>M30</my_srf:name></my_srf:Road></my_srf:road>
      |</my_srf:RoadCollection>""".stripMargin

  val inlineXmlContent1 = Json.stringify(JsString(xmlContent1))

  val jsContent1 =
    s"""{"type" : "application/gml+xml",
       |"content" : ${inlineXmlContent1}
       |}
    """.stripMargin

  // val inlineJsContent1 = Json.stringify(Json.parse(jsContent1))

  val jsOff5 =
    s"""{
       |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wfs",
       |"operations" : [{
       |  "code" : "GetCapabilities",
       |  "method" : "GET",
       |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WFS&VERSION=2.0.0"
       |  }],
       |"contents": [ $jsContent1 ],
       |"styles": [ $jsStyle1 ],
       |"uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
       |}
      """.stripMargin

  "DataType OWC:Offering GeoJSON Section 7.1.3" should {

    "<off>.code SHALL have Code identifying the requirement class identifier (URI) for the type of offering" in {
      val jsVal = Json.parse(jsOff1)

      val fromJson: JsResult[OwcOffering] = Json.fromJson[OwcOffering](jsVal)
      fromJson match {
        case JsSuccess(r: OwcOffering, path: JsPath) => println("code: " + r.code)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcOffering] = jsVal.validate[OwcOffering]
      result match {
        case s: JsSuccess[OwcOffering] => println("uuid: " + s.get.uuid.toString)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.WMS.code
      Json.parse(jsOff1_1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
      Json.parse(jsOff1_2).validate[OwcOffering].isInstanceOf[JsError] mustBe true
      Json.parse(jsOff2).validate[OwcOffering].get.code mustEqual OwcOfferingType.WMS.code
      Json.parse(jsOff3).validate[OwcOffering].get.code mustEqual OwcOfferingType.WMS.code
      Json.parse(jsOff4).validate[OwcOffering].get.code mustEqual OwcOfferingType.WMS.code
      Json.parse(jsOff5).validate[OwcOffering].get.code mustEqual OwcOfferingType.WFS.code
    }

    "<off>.operations[k] MAY have Array of operations used to invoke the service owc:OperationType (0..*)" in {
      Json.parse(jsOff1).validate[OwcOffering].get.operations mustEqual List()
      Json.parse(jsOff2).validate[OwcOffering].get.operations.length mustEqual 0
      Json.parse(jsOff3).validate[OwcOffering].get.operations.head.code mustEqual "GetCapabilities"
      Json.parse(jsOff4).validate[OwcOffering].get.operations.head.requestUrl mustEqual new java.net.URL("http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1")
      Json.parse(jsOff5).validate[OwcOffering].get.operations.head.uuid.isInstanceOf[UUID] mustBe true
    }

    "<off>.contents[k] MAY have Array of contents (inline or byRef) owc:ContentType (0..*)" in {
      Json.parse(jsOff1).validate[OwcOffering].get.contents mustEqual List()
      Json.parse(jsOff2).validate[OwcOffering].get.contents.length mustEqual 0
      Json.parse(jsOff3).validate[OwcOffering].get.contents.isEmpty mustBe true
      Json.parse(jsOff5).validate[OwcOffering].get.contents.length mustEqual 1
      Json.parse(jsOff5).validate[OwcOffering].get.contents.head.content mustEqual Some(xmlContent1)
      Json.parse(jsOff5).validate[OwcOffering].get.contents.head.mimeType mustEqual "application/gml+xml"
      Json.parse(jsOff5).validate[OwcOffering].get.contents.head.uuid.isInstanceOf[UUID] mustBe true
    }

    "<off>.styles[k] MAY have Array of style sets owc:StyleSetType (0..*)" in {
      Json.parse(jsOff1).validate[OwcOffering].get.styles mustEqual List()
      Json.parse(jsOff2).validate[OwcOffering].get.styles.length mustEqual 0
      Json.parse(jsOff3).validate[OwcOffering].get.styles.isEmpty mustBe true
      Json.parse(jsOff5).validate[OwcOffering].get.styles.length mustEqual 1
      Json.parse(jsOff5).validate[OwcOffering].get.styles.head.name mustEqual "Simple Line"
      Json.parse(jsOff5).validate[OwcOffering].get.styles.head.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsOff5).validate[OwcOffering].get.styles.head.abstrakt mustEqual Some(sldAbstrakt)
      Json.parse(jsOff5).validate[OwcOffering].get.styles.head.uuid.isInstanceOf[UUID] mustBe true
    }

    "<off>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <off>.uuid as unique identifier")
      Json.parse(jsOff1).validate[OwcOffering].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOff2).validate[OwcOffering].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOff3).validate[OwcOffering].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOff4).validate[OwcOffering].get.uuid mustEqual UUID.fromString("012c7aeb-a822-49d7-8a66-e77fa7137240")
      Json.parse(jsOff5).validate[OwcOffering].get.uuid mustEqual UUID.fromString("012c7aeb-a822-49d7-8a66-e77fa7137240")
      Json.parse(jsOff5).validate[OwcOffering].get.uuid mustEqual UUID.fromString("012c7aeb-a822-49d7-8a66-e77fa7137240")
    }
  }

  "DataType OWC:StyleSet GeoJSON Section 7.1.6" should {

    "<off>.styles[k].name SHALL have Unique name of the styleSet within a given offering" in {
      val jsStyle1 = s"""{
                        |"name": "Simple Line 1",
                        |"title": "SLD Cook Book: Simple Line 1"
                        |}
                   """.stripMargin

      val jsStyle2 = s"""{
                        |"name": "Simple Line 2",
                        |"title": "SLD Cook Book: Simple Line 2"
                        |}
                   """.stripMargin

      val jsOff1 =
        s"""{
           |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
           |"styles": [ $jsStyle1, $jsStyle2 ]
           |}
      """.stripMargin

      val jsOff2 =
        s"""{
           |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
           |"styles": [ $jsStyle1, $jsStyle1 ]
           |}
      """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.styles.length mustEqual 2
      Json.parse(jsOff2).validate[OwcOffering].get.styles.length mustEqual 1
    }
  }

  "OwcOffering GeoJson Offering Extensions" should {

    "handle WMS: http://www.opengis.net/spec/owc-geojson/1.0/req/wms" in {

      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.WMS.code

    }

    "handle WFS: http://www.opengis.net/spec/owc-geojson/1.0/req/wfs" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wfs"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.WFS.code
    }

    "handle WCS: http://www.opengis.net/spec/owc-geojson/1.0/req/wcs" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wcs"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.WCS.code
    }

    "handle WPS: http://www.opengis.net/spec/owc-geojson/1.0/req/wps" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wps"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.WPS.code
    }

    "not handle WMTS: http://www.opengis.net/spec/owc-geojson/1.0/req/wmts" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wmts"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
    }

    "handle CSW: http://www.opengis.net/spec/owc-geojson/1.0/req/csw" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/csw"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.CSW.code
    }

    "not handle GML: http://www.opengis.net/spec/owc-geojson/1.0/req/gml" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/gml"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
    }

    "not handle KML: http://www.opengis.net/spec/owc-geojson/1.0/req/kml" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/kml"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
    }

    "handle GeoTIFF: http://www.opengis.net/spec/owc-geojson/1.0/req/geotiff" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/geotiff"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.GEOTIFF.code
    }

    "not handle GMLJP2: http://www.opengis.net/spec/owc-geojson/1.0/req/gmljp2" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/gmljp2"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
    }

    "not handle GMLCOV: http://www.opengis.net/spec/owc-geojson/1.0/req/gmlcov" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/gmlcov"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
    }

    "define and handle SOS: http://www.opengis.net/spec/owc-geojson/1.0/req/sos" in {
      val jsOff1 =
        """{
          |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/sos"
          |}
        """.stripMargin

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual OwcOfferingType.SOS.code
    }

  }

  "OwcOffering Writes" should {

    "write OwcOffering GeoJSON" in {

      val res1 = Json.parse(jsOff1).validate[OwcOffering].get
      val res2 = Json.parse(jsOff2).validate[OwcOffering].get
      val res3 = Json.parse(jsOff3).validate[OwcOffering].get
      val res4 = Json.parse(jsOff4).validate[OwcOffering].get
      val res5 = Json.parse(jsOff5).validate[OwcOffering].get

      res1.toJson.validate[OwcOffering].get mustEqual res1
      res2.toJson.validate[OwcOffering].get mustEqual res2
      res3.toJson.validate[OwcOffering].get mustEqual res3
      res4.toJson.validate[OwcOffering].get mustEqual res4
      res5.toJson.validate[OwcOffering].get mustEqual res5
    }
  }

  "OwcOffering Custom" should {

    "Copy and Compare" in {

      val res1 = Json.parse(jsOff5).validate[OwcOffering].get
      val resClone = res1.newOf

      resClone must not equal res1
      resClone.sameAs(res1) mustBe true

      val resClone2 = OwcOffering.newOf(res1)

      resClone2 must not equal res1
      resClone2.sameAs(res1) mustBe true

      val resCaseCopy = res1.copy()
      resCaseCopy mustEqual res1
      resCaseCopy must not equal resClone
    }
  }
}
