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

class OwcOperationSpec extends WordSpec with MustMatchers with LazyLogging{

  val jsOp1 = """{
                |  "code" : "GetCapabilities",
                |  "method" : "GET",
                |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
                |}""".stripMargin

  val jsOp1_1 = """{
                  |  "code" : "GetCapabilities",
                  |  "href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
                  |}""".stripMargin

  val jsOp1_2 = """{
                  |  "code" : "GetCapabilities",
                  |  "method" : "GET",
                  |  "href" : "htt://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
                  |}""".stripMargin

  val jsOp2 = """{
                | "code" : "GetRecords",
                | "type" : "application/xml",
                | "method" : "POST",
                | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2"
                |}""".stripMargin

  val jsOp2_1 = """{
                  | "code" : "GetRecords",
                  | "type" : "application/xml",
                  | "method" : "",
                  | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2"
                  |}""".stripMargin

  val jsOp2_2 = """{
                  | "code" : "GetRecords",
                  | "type" : "application/xml",
                  | "method" : "STREAM",
                  | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2"
                  |}""".stripMargin

  val jsOp2_3 = """{
                  | "code" : "GetRecords",
                  | "type" : "not-good-mimetype",
                  | "method" : "POST",
                  | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2"
                  |}""".stripMargin

  val jsOp2_4 = """{
                  | "code" : "GetRecords",
                  | "type" : "application/xml",
                  | "method" : "POST",
                  | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2",
                  | "uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                  |}""".stripMargin

  val cswRequestContent = """"<csw:GetRecords maxRecords="10"
                            | outputFormat="application/xml"
                            | outputSchema="http://www.isotc211.org/2005/gmd" resultType="results"
                            | service="CSW" startPosition="1" version="2.0.2"
                            | xmlns:ogc="http://www.opengis.net/ogc"
                            | xmlns:csw="http://www.opengis.net/cat/csw/2.0.2">
                            | <csw:Query>
                            |  <csw:ElementSetName typeNames="csw:Record">full</csw:ElementSetName>
                            |  <csw:Constraint version="1.1.0">
                            |   <ogc:Filter>
                            |    <ogc:PropertyIsEqualTo>
                            |     <ogc:PropertyName>csw:Record/@id</ogc:PropertyName>
                            |     <ogc:Literal>9496276a-4f6e-47c1-94bbf604245fac57</ogc:Literal>
                            |    </ogc:PropertyIsEqualTo>
                            |   </ogc:Filter>
                            |  </csw:Constraint>
                            | </csw:Query>
                            |</csw:GetRecords>""".stripMargin

  val inlineRequestContent = Json.stringify(JsString(cswRequestContent))

  val jsOp3 = s"""{
                 | "code" : "GetRecords",
                 | "type" : "application/xml",
                 | "method" : "POST",
                 | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2",
                 | "request":{
                 |   "type" : "application/xml",
                 |   "content" : ${inlineRequestContent}
                 |  }
                 |}""".stripMargin

  val sldContent = """<StyledLayerDescriptor version="1.0.0"
                     | xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
                     | xmlns:xlink="http://www.w3.org/1999/xlink"
                     | xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     | xsi:schemaLocation="http://www.opengis.net/sld
                     |   ../../../sld/1.1/StyledLayerDescriptor.xsd">
                     |<NamedLayer><Name>Simple Line</Name>
                     |<UserStyle><Title>SLD Cook Book: Simple Line</Title>
                     |<FeatureTypeStyle><Rule><LineSymbolizer><Stroke>
                     |<CssParameter name="stroke">#000000</CssParameter>
                     |<CssParameter name="strokewidth">3</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle>
                     |</UserStyle></NamedLayer></StyledLayerDescriptor>""".stripMargin

  val inlineResultContent = Json.stringify(JsString(sldContent))

  val jsOp4 = s"""{
                 | "code" : "GetRecords",
                 | "type" : "application/xml",
                 | "method" : "POST",
                 | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2",
                 | "result":{
                 |   "type" : "application/xml",
                 |   "content" : ${inlineResultContent},
                 |   "uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
                 |  }
                 |}""".stripMargin

  val jsOp4_1 = s"""{
                   | "code" : "GetRecords",
                   | "type" : "application/xml",
                   | "method" : "POST",
                   | "href" : "http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2",
                   | "result":{
                   |   "type" : "application/xml",
                   |   "content" : ${inlineResultContent},
                   |   "uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
                   |  },
                   |  "uuid": "a12c7aeb-a822-49d7-8a66-e77fa713724f"
                   |}""".stripMargin

  "DataType OWC:Operation GeoJSON Section 7.1.4" should {

    "<op>.code SHALL have Code identifying the type of Operation" in {
      val jsVal = Json.parse(jsOp1)

      val fromJson: JsResult[OwcOperation] = Json.fromJson[OwcOperation](jsVal)
      fromJson match {
        case JsSuccess(r: OwcOperation, path: JsPath) => println("code: " + r.code)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcOperation] = jsVal.validate[OwcOperation]
      result match {
        case s: JsSuccess[OwcOperation] => println("method: " + s.get.method)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsOp1).validate[OwcOperation].get.code mustEqual "GetCapabilities"
      Json.parse(jsOp1_1).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp2).validate[OwcOperation].get.code mustEqual "GetRecords"
      Json.parse(jsOp3).validate[OwcOperation].get.code mustEqual "GetRecords"
      Json.parse(jsOp4).validate[OwcOperation].get.code mustEqual "GetRecords"
    }

    "<op>.method SHALL have Code identifying the HTTP verb type of Operation. String, Example values are GET and POST." in {
      Json.parse(jsOp1).validate[OwcOperation].get.method mustEqual "GET"
      Json.parse(jsOp2).validate[OwcOperation].get.method mustEqual "POST"
      Json.parse(jsOp2_1).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp2_2).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp3).validate[OwcOperation].get.method mustEqual "POST"
      Json.parse(jsOp4).validate[OwcOperation].get.method mustEqual "POST"
    }

    "<op>.type MAY have MIME type of the expected results String, that contains a MIME media type (0..1)" in {
      Json.parse(jsOp1).validate[OwcOperation].get.mimeType mustEqual None
      Json.parse(jsOp2).validate[OwcOperation].get.mimeType mustEqual Some("application/xml")
      Json.parse(jsOp2_1).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp2_2).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp2_3).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp3).validate[OwcOperation].get.mimeType mustEqual Some("application/xml")
      Json.parse(jsOp4).validate[OwcOperation].get.mimeType mustEqual Some("application/xml")
    }

    "<op>.href SHALL have Full Service Request URL for an HTTP GET, and request URL for HTTP POST" in {
      Json.parse(jsOp1).validate[OwcOperation].get.requestUrl mustEqual new URL("http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1")
      Json.parse(jsOp1_2).validate[OwcOperation].isInstanceOf[JsError] mustBe true
      Json.parse(jsOp2).validate[OwcOperation].get.requestUrl mustEqual new URL("http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2")
      Json.parse(jsOp3).validate[OwcOperation].get.requestUrl mustEqual new URL("http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2")
      Json.parse(jsOp4).validate[OwcOperation].get.requestUrl mustEqual new URL("http://www.someserver.com/wrs.cgi?service=CSW&request=GetCapabilities&VERSION=2.0.2")
    }

    "<op>.request MAY have Optional request body content of owc:ContentType (0..1)" in {
      logger.info("Not necessarily XML as the content is defined by MIME-type. " +
        "If the content is text/xml or application/*+xml it SHALL be present as a XML fragment (without the <?xml... header) " +
        "and the encoding SHALL be the same as the feed.")
      val a = Json.parse(s"""{
                           |  "type" : "application/xml",
                           |  "content" : ${inlineRequestContent}
                           |}
                         """.stripMargin).validate[OwcContent].get



      Json.parse(jsOp2).validate[OwcOperation].get.request mustEqual None
      Json.parse(jsOp3).validate[OwcOperation].get.request.get.mimeType mustEqual a.mimeType
      Json.parse(jsOp3).validate[OwcOperation].get.request.get.content mustEqual a.content

    }

    "<op>.result MAY have Optional Result Payload of the operation, owc:ContentType (0..1)" in {
      logger.info("Not necessarily XML as the content is defined by MIME-type. " +
        "If the content is text/xml or application/*+xml it SHALL be present as a XML fragment (without the <?xml... header) " +
        "and the encoding SHALL be the same as the feed.")

      val b = Json.parse(s"""{
                            | "type" : "application/xml",
                            | "content" : ${inlineResultContent},
                            | "uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
                            |}""".stripMargin).validate[OwcContent].get

      Json.parse(jsOp2).validate[OwcOperation].get.result mustEqual None
      Json.parse(jsOp4).validate[OwcOperation].get.result mustEqual Some(b)
    }

    "<op>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <op>.uuid as unique identifier")
      Json.parse(jsOp1).validate[OwcOperation].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOp2).validate[OwcOperation].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOp2_4).validate[OwcOperation].get.uuid mustEqual UUID.fromString("a12c7aeb-a822-49d7-8a66-e77fa713724f")
      Json.parse(jsOp3).validate[OwcOperation].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOp4).validate[OwcOperation].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsOp4_1).validate[OwcOperation].get.uuid mustEqual UUID.fromString("a12c7aeb-a822-49d7-8a66-e77fa713724f")
    }
  }

  "OwcOperation Writes" should {

    "write OwcOperation GeoJSON" in {

      val res1 = Json.parse(jsOp1).validate[OwcOperation].get
      val res2 = Json.parse(jsOp2).validate[OwcOperation].get
      val res3 = Json.parse(jsOp3).validate[OwcOperation].get
      val res4 = Json.parse(jsOp4).validate[OwcOperation].get

      res1.toJson.validate[OwcOperation].get mustEqual res1
      res2.toJson.validate[OwcOperation].get mustEqual res2
      res3.toJson.validate[OwcOperation].get mustEqual res3
      res4.toJson.validate[OwcOperation].get mustEqual res4
    }
  }

  "OwcOperation Custom" should {

    "Copy and Compare" in {

      val res1 = Json.parse(jsOp3).validate[OwcOperation].get
      val resClone = res1.newOf

      resClone must not equal res1
      resClone.sameAs(res1) mustBe true

      val resClone2 = OwcOperation.newOf(res1)

      resClone2 must not equal res1
      resClone2.sameAs(res1) mustBe true

      val resCaseCopy = res1.copy()
      resCaseCopy mustEqual res1
      resCaseCopy must not equal resClone
    }
  }
}
