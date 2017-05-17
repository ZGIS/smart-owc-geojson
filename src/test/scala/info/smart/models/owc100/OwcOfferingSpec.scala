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

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._

class OwcOfferingSpec extends WordSpec with MustMatchers with LazyLogging {

  "DataType OWC:Offering GeoJSON Section 7.1.3" should {

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

    val inlineStyleSet3 = Json.stringify(Json.parse(jsStyle1))

    val jsOff4 =
      s"""{
        |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
        |"operations" : [{
        |  "code" : "GetCapabilities",
        |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
        |  }],
        |"styles": [ $inlineStyleSet3 ],
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

    val inlineJsContent1 = Json.stringify(Json.parse(jsContent1))

    val jsOff5 =
      s"""{
         |"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wfs",
         |"operations" : [{
         |  "code" : "GetCapabilities",
         |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WFS&VERSION=2.0.0"
         |  }],
         |"content": [ $inlineJsContent1 ],
         |"styles": [ $inlineStyleSet3 ],
         |"uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
         |}
      """.stripMargin

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

      Json.parse(jsOff1).validate[OwcOffering].get.code mustEqual new java.net.URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")
      Json.parse(jsOff1_1).validate[OwcOffering].isInstanceOf[JsError] mustBe true
      Json.parse(jsOff1_2).validate[OwcOffering].isInstanceOf[JsError] mustBe true
      Json.parse(jsOff2).validate[OwcOffering].get.code mustEqual new java.net.URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")
      Json.parse(jsOff3).validate[OwcOffering].get.code mustEqual new java.net.URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")
      Json.parse(jsOff4).validate[OwcOffering].get.code mustEqual new java.net.URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")
      Json.parse(jsOff5).validate[OwcOffering].get.code mustEqual new java.net.URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wfs")
    }

    "<off>.operations[k] MAY have Array of operations used to invoke the service owc:OperationType (0..*)" in {

    }

    "<off>.contents[k] MAY have Array of contents (inline or byRef) owc:ContentType (0..*)" in {

    }

    "<off>.styles[k] MAY have Array of style sets owc:StyleSetType (0..*)" in {

    }

    "<off>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <off>.uuid as unique identifier")
    }
  }

  "DataType OWC:StyleSet GeoJSON Section 7.1.6" should {

    "<off>.styles[k].name SHALL have Unique name of the styleSet within a given offering" in {

    }
  }

  "OwcOffering GeoJson Offering Extensions" should {

    "handle WMS: http://www.opengis.net/spec/owc-geojson/1.0/req/wms" in {

    }

    "handle WFS: http://www.opengis.net/spec/owc-geojson/1.0/req/wfs" in {

    }

    "handle WCS: http://www.opengis.net/spec/owc-geojson/1.0/req/wcs" in {

    }

    "handle WPS: http://www.opengis.net/spec/owc-geojson/1.0/req/wps" in {

    }

    "not handle WMTS: http://www.opengis.net/spec/owc-geojson/1.0/req/wmts" in {

    }

    "handle CSW: http://www.opengis.net/spec/owc-geojson/1.0/req/csw" in {

    }

    "not handle GML: http://www.opengis.net/spec/owc-geojson/1.0/req/gml" in {

    }

    "not handle KML: http://www.opengis.net/spec/owc-geojson/1.0/req/kml" in {

    }

    "handle GeoTIFF: http://www.opengis.net/spec/owc-geojson/1.0/req/geotiff" in {

    }

    "not handle GMLJP2: http://www.opengis.net/spec/owc-geojson/1.0/req/gmljp2" in {

    }

    "not handle GMLCOV: http://www.opengis.net/spec/owc-geojson/1.0/req/gmlcov" in {

    }

    "define and handle SOS: http://www.opengis.net/spec/owc-geojson/1.0/req/sos" in {

    }

  }
}