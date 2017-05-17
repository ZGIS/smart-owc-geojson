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

class OwcStyleSetSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType OWC:StyleSet GeoJSON Section 7.1.6" should {

    val jsStyle1 = """{
        |"name": "Simple Line"
        |}
      """.stripMargin

    val jsStyle2 = """{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line"
                     |}
                   """.stripMargin

    val abstrakt = "SLD Cook Book: Simple Line extracted from http://docs.geoserver.org/latest/en/user/_downloads/line_simpleline.sld"

    val jsStyle3 = s"""{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line",
                     |"abstract": "$abstrakt"
                     |}
                   """.stripMargin

    val jsStyle4 = s"""{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line",
                     |"abstract": "$abstrakt",
                     |"default": true
                     |}
                   """.stripMargin


    val jsStyle5 = s"""{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line",
                     |"abstract": "$abstrakt",
                     |"default": true,
                     |"legendURL": "http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png",
                     |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
                     |}
                   """.stripMargin

    val jsStyle6 = s"""{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line",
                     |"abstract": "$abstrakt",
                     |"default": true,
                     |"legendURL": "http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png",
                     |"content": {
                     |  "type": "application/vnd.ogc.sld+xml",
                     |  "href": "http://docs.geoserver.org/latest/en/user/_downloads/line_simpleline.sld"
                     |  }
                     |}
                   """.stripMargin

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
                       |</UserStyle></NamedLayer></StyledLayerDescriptor>"""

    val inlineContent = Json.stringify(JsString(sldContent))


    val jsStyle7 = s"""{
                     |"name": "Simple Line",
                     |"title": "SLD Cook Book: Simple Line",
                     |"abstract": "$abstrakt",
                     |"default": true,
                     |"legendURL": "http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png",
                     |"content": {
                     |  "type": "application/vnd.ogc.sld+xml",
                     |  "content" : ${inlineContent},
                     |  "uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
                     |  },
                     |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
                     |}
                   """.stripMargin

    "<style>.name SHALL have Unique name of the styleSet within a given offering" in {
      logger.info(" Uniqueness of name within a given offering deferred to OwcOfferingSpec")
      val jsVal = Json.parse(jsStyle2)

      val fromJson: JsResult[OwcStyleSet] = Json.fromJson[OwcStyleSet](jsVal)
      fromJson match {
        case JsSuccess(r: OwcStyleSet, path: JsPath) => println("name: " + r.name)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcStyleSet] = jsVal.validate[OwcStyleSet]
      result match {
        case s: JsSuccess[OwcStyleSet] => println("title: " + s.get.title)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsStyle1).validate[OwcStyleSet].isInstanceOf[JsError] mustBe true
      Json.parse(jsStyle2).validate[OwcStyleSet].get.name mustEqual "Simple Line"
      Json.parse(jsStyle3).validate[OwcStyleSet].get.name mustEqual "Simple Line"
      Json.parse(jsStyle4).validate[OwcStyleSet].get.name mustEqual "Simple Line"
      Json.parse(jsStyle5).validate[OwcStyleSet].get.name mustEqual "Simple Line"
      Json.parse(jsStyle6).validate[OwcStyleSet].get.name mustEqual "Simple Line"
      Json.parse(jsStyle7).validate[OwcStyleSet].get.name mustEqual "Simple Line"
    }

    "<style>.title SHALL have Unique Human Readable title of the styleSet within a given offering" in {
      logger.info(" Uniqueness of title within a given offering deferred to OwcOfferingSpec")
      Json.parse(jsStyle2).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsStyle3).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsStyle4).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsStyle5).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsStyle6).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
      Json.parse(jsStyle7).validate[OwcStyleSet].get.title mustEqual "SLD Cook Book: Simple Line"
    }

    "<style>.abstract MAY have Description of the styleSet (0..1)" in {
      Json.parse(jsStyle2).validate[OwcStyleSet].get.abstrakt mustEqual None
      Json.parse(jsStyle3).validate[OwcStyleSet].get.abstrakt mustEqual Some(abstrakt)
      Json.parse(jsStyle4).validate[OwcStyleSet].get.abstrakt mustEqual Some(abstrakt)
      Json.parse(jsStyle5).validate[OwcStyleSet].get.abstrakt mustEqual Some(abstrakt)
      Json.parse(jsStyle6).validate[OwcStyleSet].get.abstrakt mustEqual Some(abstrakt)
      Json.parse(jsStyle7).validate[OwcStyleSet].get.abstrakt mustEqual Some(abstrakt)
    }

    "<style>.default MAY declare whether this styleSet to be applied as default when the service is invoked. 'true' or 'false' (0..1)" in {
      Json.parse(jsStyle2).validate[OwcStyleSet].get.default mustEqual None
      Json.parse(jsStyle3).validate[OwcStyleSet].get.default mustEqual None
      Json.parse(jsStyle4).validate[OwcStyleSet].get.default mustEqual Some(true)
      Json.parse(jsStyle5).validate[OwcStyleSet].get.default mustEqual Some(true)
      Json.parse(jsStyle6).validate[OwcStyleSet].get.default mustEqual Some(true)
      Json.parse(jsStyle7).validate[OwcStyleSet].get.default mustEqual Some(true)
    }

    "<style>.legendURL MAY have URL of a legend image for the styleSet (0..1)" in {
      Json.parse(jsStyle2).validate[OwcStyleSet].get.legendUrl mustEqual None
      Json.parse(jsStyle3).validate[OwcStyleSet].get.legendUrl mustEqual None
      Json.parse(jsStyle4).validate[OwcStyleSet].get.legendUrl mustEqual None
      Json.parse(jsStyle5).validate[OwcStyleSet].get.legendUrl mustEqual Some(new java.net.URL("http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png"))
      Json.parse(jsStyle6).validate[OwcStyleSet].get.legendUrl mustEqual Some(new java.net.URL("http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png"))
      Json.parse(jsStyle7).validate[OwcStyleSet].get.legendUrl mustEqual Some(new java.net.URL("http://docs.geoserver.org/latest/en/user/_images/line_simpleline1.png"))
    }

    "<style>.content MAY have The inline or a external reference to the styleSet definition, of owc:ContentType (0..1)" in {

      val a = Json.parse("""{
        |  "type": "application/vnd.ogc.sld+xml",
        |  "href": "http://docs.geoserver.org/latest/en/user/_downloads/line_simpleline.sld"
        |}
      """.stripMargin).validate[OwcContent].get

      val b = Json.parse(s"""{
        |  "type": "application/vnd.ogc.sld+xml",
        |  "content" : ${inlineContent},
        |  "uuid": "012c7aeb-a822-49d7-8a66-e77fa7137240"
        |  }
      """.stripMargin).validate[OwcContent].get

      Json.parse(jsStyle5).validate[OwcStyleSet].get.content mustEqual None
      Json.parse(jsStyle6).validate[OwcStyleSet].get.content.get.mimeType mustEqual a.mimeType
      Json.parse(jsStyle6).validate[OwcStyleSet].get.content.get.url mustEqual a.url
      Json.parse(jsStyle7).validate[OwcStyleSet].get.content mustEqual Some(b)
    }

    "<style>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <style>.uuid as unique identifier")
      Json.parse(jsStyle2).validate[OwcStyleSet].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsStyle3).validate[OwcStyleSet].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsStyle4).validate[OwcStyleSet].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsStyle5).validate[OwcStyleSet].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")
      Json.parse(jsStyle6).validate[OwcStyleSet].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsStyle7).validate[OwcStyleSet].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")
    }
  }
}