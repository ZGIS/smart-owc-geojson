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

class OwcLinkSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType 'links' GeoJSON Section 7.1.10" should {

    val jsLink1 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.html",
         |"title" : "Information for the entry 2009-01-23 09:08:56",
         |"rel": "alternate"
         |}
      """.stripMargin

    val jsLink1_1 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.html",
         |"title" : "",
         |"rel": "alternate"
         |}
      """.stripMargin

    val jsLink2 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.xml",
         |"type" : "application/xml",
         |"length" : 435,
         |"title" : "XML metadata file for the entry 2009-01-23 09:08:56",
         |"rel": "via"
         |}
      """.stripMargin

    val jsLink2_1 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.xml",
         |"type" : "application/xml",
         |"length" : "435",
         |"title" : "XML metadata file for the entry 2009-01-23 09:08:56",
         |"rel": "via"
         |}
      """.stripMargin

    val jsLink3 =
      s"""{"href" : "http://www.acme.com/collections/algal.xml",
         |"type" : "application/xml",
         |"title" : "Algal XML metadata",
         |"lang" : "en",
         |"rel": "via",
         |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
         |}
      """.stripMargin

    val jsLink3_1 =
      s"""{"href" : "http://www.acme.com/collections/algal.xml",
         |"type" : "application/xml",
         |"title" : "Algal XML metadata",
         |"lang" : "NOLANG",
         |"rel": "via",
         |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
         |}
      """.stripMargin

    val jsLink4 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.png",
         |"type" : "image/png",
         |"length" : 12321,
         |"title" : " Quicklook for the entry 2009-01-23 09:08:56",
         |"rel": "icon",
         |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
         |}
      """.stripMargin

    val jsLink4_1 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.png",
         |"type" : "image/png",
         |"length" : 12321,
         |"title" : " Quicklook for the entry 2009-01-23 09:08:56",
         |"rel": "boogie",
         |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
         |}
      """.stripMargin

    val jsLink5 =
      s"""{"href" : "http://www.acme.com/products/algal20090123090856.hdf",
         |"type" : "application/x-hdf5",
         |"length" : 453123432,
         |"title" : "HDF file for the entry 2009-01-23 09:08:56",
         |"rel": "enclosure",
         |"uuid": "b9ea2498-fb32-40ef-91ef-0ba00060fe64"
         |}
      """.stripMargin

    "<xz>.href SHALL have URI describing the related resource" in {

      val jsVal = Json.parse(jsLink1)

      val fromJson: JsResult[OwcLink] = Json.fromJson[OwcLink](jsVal)
      fromJson match {
        case JsSuccess(r: OwcLink, path: JsPath) => println("href: " + r.href)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcLink] = jsVal.validate[OwcLink]
      result match {
        case s: JsSuccess[OwcLink] => println("rel: " + s.get.rel)
        case e: JsError => logger.error("Errors: " + JsError.toJson(e).toString())
      }

      Json.parse(jsLink1).validate[OwcLink].get.href mustEqual new URL("http://www.acme.com/products/algal20090123090856.html")
      Json.parse(jsLink2).validate[OwcLink].get.href mustEqual new URL("http://www.acme.com/products/algal20090123090856.xml")
      Json.parse(jsLink3).validate[OwcLink].get.href mustEqual new URL("http://www.acme.com/collections/algal.xml")
      Json.parse(jsLink4).validate[OwcLink].get.href mustEqual new URL("http://www.acme.com/products/algal20090123090856.png")
      Json.parse(jsLink5).validate[OwcLink].get.href mustEqual new URL("http://www.acme.com/products/algal20090123090856.hdf")
      // Json.parse(jsLink2).validate[OwcLink].isInstanceOf[JsError] mustBe true
    }

    "<xz>.type MAY have media type of the representation that is expected for when the value of the href attribute is dereferenced (0..1" in {
      Json.parse(jsLink1).validate[OwcLink].get.mimeType mustEqual None
      Json.parse(jsLink2).validate[OwcLink].get.mimeType mustEqual Some("application/xml")
      Json.parse(jsLink5).validate[OwcLink].get.mimeType mustEqual Some("application/x-hdf5")
    }

    "<xz>.lang MAY have Language of the resource pointed to by the href attribute, an RFC-3066 code (0..1)" in {
      Json.parse(jsLink1).validate[OwcLink].get.lang mustEqual None
      Json.parse(jsLink3).validate[OwcLink].get.lang mustEqual Some("en")
      Json.parse(jsLink3_1).validate[OwcLink].isInstanceOf[JsError] mustBe true
    }

    "<xz>.title MAY have Human-readable information about the link (0..1)" in {
      Json.parse(jsLink1).validate[OwcLink].get.title mustEqual Some("Information for the entry 2009-01-23 09:08:56")
      Json.parse(jsLink1_1).validate[OwcLink].isInstanceOf[JsError] mustBe true
      Json.parse(jsLink2).validate[OwcLink].get.title mustEqual Some("XML metadata file for the entry 2009-01-23 09:08:56")
    }

    "<xz>.length MAY have expected content length in octets of the representation returned " +
      "when the URI in the href attribute is dereferenced. non-negative Integer (0..1)" in {
      Json.parse(jsLink1).validate[OwcLink].get.length mustEqual None
      Json.parse(jsLink2).validate[OwcLink].get.length mustEqual Some(435)
      Json.parse(jsLink2_1).validate[OwcLink].isInstanceOf[JsError] mustBe true
    }

    "<xz>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <xz>.uuid as unique identifier")

      Json.parse(jsLink1).validate[OwcLink].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsLink2).validate[OwcLink].get.uuid.isInstanceOf[UUID] mustBe true
      Json.parse(jsLink4).validate[OwcLink].get.uuid mustEqual UUID.fromString("b9ea2498-fb32-40ef-91ef-0ba00060fe64")

      logger.info("MAY contain <xz>.rel as our internal relations indicator")

      Json.parse(jsLink1).validate[OwcLink].get.rel mustEqual "alternate"
      Json.parse(jsLink3).validate[OwcLink].get.rel mustEqual "via"
      Json.parse(jsLink4).validate[OwcLink].get.rel mustEqual "icon"
      // Json.parse(jsLink4_1).validate[OwcLink].isInstanceOf[JsError] mustBe true
      Json.parse(jsLink4_1).validate[OwcLink].get.rel mustEqual "alternate"
      Json.parse(jsLink5).validate[OwcLink].get.rel mustEqual "enclosure"
    }
  }
}