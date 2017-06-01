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
import java.time.{OffsetDateTime, ZoneOffset}
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._
import uk.gov.hmrc.emailaddress.EmailAddress

class OwcContextSpec extends WordSpec with MustMatchers with LazyLogging {

  private lazy val owcResource1: URL = this.getClass().getResource("/owc100/owc1.geojson")
  private lazy val owcResource2: URL = this.getClass().getResource("/owc100/owc2.geojson")
  private lazy val owcResource3: URL = this.getClass().getResource("/owc100/owc3.geojson")

  private val jsonTestCollection1: JsValue = Json.parse(scala.io.Source.fromURL(owcResource1).getLines.mkString)
  private val jsonTestCollection2: JsValue = Json.parse(scala.io.Source.fromURL(owcResource2).getLines.mkString)
  private val jsonTestCollection3: JsValue = Json.parse(scala.io.Source.fromURL(owcResource3).getLines.mkString)

  "OwcContext GeoJson Dependency http://www.opengis.net/spec/owc/1.0/core" should {

    "TBD: comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/geojsonRules" in {
      logger.info("TBD: An OWS Context document encoded in GeoJSON (GeoJSON Context Document) " +
        "SHALL comply with the rules specified in [IETF GeoJSON]")
    }

    "TBD: comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/mime-type" in {
      logger.info("TBD: GeoJSON OWS Context documents SHALL adopt the GeoJSON MIMEtype application/vnd.geo+json")
    }

    "TBD: comply with  Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/file-extension" in {
      logger.info("TBD: GeoJSON OWS Context documents using GeoJSON SHALL use the file extension of '.geojson' or '.json'")
    }

    "comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/owc-encoding" in {
      logger.info("GeoJSON encoded OWS Context documents SHALL comply with the encoding rules given in the section 7.1.1. - " +
        "Tested in: Class OWC:Context GeoJson Section 7.1.1")
    }

  }

  "Class OWC:Context GeoJson Section 7.1.1" should {

    "<xz>.type SHALL have the value FeatureCollection" in {
      (jsonTestCollection1 \ "type").as[String] mustEqual "FeatureCollection"
      (jsonTestCollection2 \ "type").as[String] mustEqual "FeatureCollection"
      (jsonTestCollection3 \ "type").as[String] mustEqual "FeatureCollection"
    }


    "<xz>.properties.links.profiles SHALL have the href value 'http://www.opengis.net/spec/owc-geojson/1.0/req/core'" in {
      jsonTestCollection1.validate[OwcContext].get.specReference(0).href mustEqual new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/core")
      jsonTestCollection2.validate[OwcContext].get.specReference(0).href mustEqual new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/core")
      jsonTestCollection3.validate[OwcContext].get.specReference(0).href mustEqual new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/core")

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get
        .specReference(0).href mustEqual new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/core")
    }

    "<xz>.properties.lang SHALL have RFC-3066 language code" in {
      jsonTestCollection1.validate[OwcContext].get.language mustEqual "en"
      jsonTestCollection2.validate[OwcContext].get.language mustEqual "en"
      jsonTestCollection3.validate[OwcContext].get.language mustEqual "en"

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.language mustEqual "en"
    }

    "<xz>.id SHALL contain a URI value, with trailing slash" in {
      jsonTestCollection1.validate[OwcContext].get.id mustEqual new URL("http://www.opengis.net/owc/1.0/examples/wps_52north/")
      jsonTestCollection2.validate[OwcContext].get.id mustEqual new URL("http://ows-9.compusult.net/wes/serviceManagerCSW/csw/0006276a-4f6e-47c1-94bb-f604245fac57/")
      jsonTestCollection3.validate[OwcContext].get.id mustEqual new URL("http://portal.smart-project.info/context/smart-sac/")

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.id mustEqual new URL("http://portal.smart-project.info/context/smart-sac/")
    }

    "<xz>.properties.title SHALL contain Title for the Context document" in {
      jsonTestCollection1.validate[OwcContext].get.title mustEqual "WPS 52North example"

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.title mustEqual "SMART Case Studies"
    }

    "<xz>.properties.subtitle MAY contain Abstract for the Context document (0..1)" in {
      jsonTestCollection1.validate[OwcContext].get.subtitle mustEqual Some("WPS 52North example")

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.subtitle mustEqual Some("SMART Case Studies Informative Layers, Sel. Geophysics, GW-SW Interaction, FODTS, Novel Tracers, DataVis and SOS")
    }

    "<xz>.properties.updated SHALL contain Date of a creation or update of the Context document RFC-3339 date" in {
      logger.info("RFC 3339 defines a profile of ISO 8601 for use in Internet protocols and standards. " +
        "It explicitly excludes durations and dates before the common era. " +
        "The more complex formats such as week numbers and ordinal days are not permitted.")
      // "2012-11-04T17:26:23Z"
      jsonTestCollection1.validate[OwcContext].get.updateDate mustEqual
        OffsetDateTime.of(2012, 11, 4, 17, 26, 23, 0,  ZoneOffset.ofHours(0))

      // 2012-02-21T11:58:23Z
      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.updateDate mustEqual
        OffsetDateTime.of(2012, 2, 21, 11, 58, 23, 0,  ZoneOffset.ofHours(0))
    }

    "<xz>.properties.authors.name MAY contain primarily responsible entity, string (0..*)" in {

      jsonTestCollection1.validate[OwcContext].get.author.isEmpty mustBe false
      jsonTestCollection2.validate[OwcContext].get.author.isEmpty mustBe true
      jsonTestCollection3.validate[OwcContext].get.author.length mustEqual 1
      jsonTestCollection3.validate[OwcContext].get.author.head.name mustEqual Some("Alex Kmoch")
      jsonTestCollection3.validate[OwcContext].get.author.head.uri mustEqual Some(new URL("https://www.gns.cri.nz"))
      jsonTestCollection3.validate[OwcContext].get.author.head.email mustEqual Some(EmailAddress("a.kmoch@gns.cri.nz"))
    }

    "<xz>.properties.authors.name MUST contain one or more elements on the properties.author array " +
      "unless all of the entries of the features array contain one or more elements on the properties.authors array" in {

      val owc = jsonTestCollection1.validate[OwcContext].get
      val failedAuthor = owc.copy(author = List())
      failedAuthor.toJson.validate[OwcContext].isInstanceOf[JsError] mustBe true
    }

    "<xz>.properties.publisher MAY contain primarily responsible entity, string (0..1)" in {
      jsonTestCollection1.validate[OwcContext].get.publisher mustEqual Some("OGC and 52North")
      jsonTestCollection3.validate[OwcContext].get.publisher mustEqual None

      jsonTestCollection1.validate[OwcContext].get
        .toJson.validate[OwcContext].get.publisher mustEqual Some("OGC and 52North")

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.publisher mustEqual None
    }

    "<xz>.properties.generator MAY contain Tool/application used to create the context doc, OWC:CreatorApplication (0..1)" in {
      logger.info("Tested in: DataType OWC:Creator GeoJSON Section 7.1.7")
    }

    "<xz>.properties.display MAY contain Tool/application used to create the context doc, OWC:CreatorDisplay (0..1)" in {
      logger.info("Tested in: DataType OWC:Creator GeoJSON Section 7.1.7")
    }

    "<xz>.properties.rights MAY contain Information about rights held in and over the Context document (0..1)" in {
      jsonTestCollection1.validate[OwcContext].get.rights mustEqual None
      jsonTestCollection3.validate[OwcContext].get.rights mustEqual
        Some("Copyright (c) 2012. Some rights reserved. This feed licensed under a Creative Commons Attribution 3.0 License.")

      jsonTestCollection1.validate[OwcContext].get
        .toJson.validate[OwcContext].get.rights mustEqual None

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.rights mustEqual
        Some("Copyright (c) 2012. Some rights reserved. This feed licensed under a Creative Commons Attribution 3.0 License.")
    }

    "<xz>.bbox MAY contain Geographic Area of interest of the Context document according to the GeoJSON 'bbox' definition (0..1)" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      jsonTestCollection1.validate[OwcContext].get.areaOfInterest mustEqual None
      jsonTestCollection2.validate[OwcContext].get.areaOfInterest mustEqual Some(jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0))
      jsonTestCollection3.validate[OwcContext].get.areaOfInterest mustEqual Some(jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0))
    }

    "<xz>.date MAY contain Date or range of dates relevant, representing a date according to the ISO-8601 format (0..1)" in {
      jsonTestCollection1.validate[OwcContext].get.timeIntervalOfInterest mustEqual Some(List(
        OffsetDateTime.of(2012, 11, 4, 17, 26, 23, 0,  ZoneOffset.ofHours(0))))
      jsonTestCollection2.validate[OwcContext].get.timeIntervalOfInterest mustEqual None
      jsonTestCollection3.validate[OwcContext].get.timeIntervalOfInterest mustEqual Some(List(
        OffsetDateTime.of(2011, 11, 4, 0, 1, 23, 0, ZoneOffset.ofHours(0)),
        OffsetDateTime.of(2017, 12, 5, 17, 28, 56, 0, ZoneOffset.ofHours(0))
      ))
    }

    "<xz>.properties.categories.term MAY contain keywords related to this context document. " +
      "It MAY have a related code-list that is identified by the scheme attribute (0..*)" in {
      jsonTestCollection1.validate[OwcContext].get.keyword.isEmpty mustBe true
      jsonTestCollection3.validate[OwcContext].get.keyword.length mustEqual 5
      jsonTestCollection3.validate[OwcContext].get.keyword.head.term mustEqual "sac_add"
      jsonTestCollection3.validate[OwcContext].get.keyword.head.scheme mustEqual Some("http://gw-hub.info/vocab/view-groups")
      jsonTestCollection3.validate[OwcContext].get.keyword.head.label mustEqual Some("Informative Layers")
    }

    "<xz>.features MAY contain Resources available on the Context document, Features array element as defined in Section 7.1.2 (0..*)" in {
      jsonTestCollection1.validate[OwcContext].get.resource.length mustEqual 1
      jsonTestCollection2.validate[OwcContext].get.resource.length mustEqual 1
      jsonTestCollection3.validate[OwcContext].get.resource.length mustEqual 1
    }

    "Not used: <xz>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("Not used: MAY contain <xz>.uuid, but in Ows:Context we use <xz>.id as unique identifier")
    }
  }

  "OwcContext Writes" should {

    "write OwcContext GeoJSON" in {

      val res1 = jsonTestCollection1.validate[OwcContext].get
      val res2 = jsonTestCollection2.validate[OwcContext].get
      val res3 = jsonTestCollection3.validate[OwcContext].get

      res1.toJson.validate[OwcContext].get mustEqual res1
      res2.toJson.validate[OwcContext].get mustEqual res2
      res3.toJson.validate[OwcContext].get mustEqual res3
    }
  }

  "DataType OWC:Creator GeoJSON Section 7.1.7" should {

    "<xz>.properties.generator MAY have The name, reference and version of the creator application " +
      "used to create the context document owc:CreatorApplication (0..1)" in {
      logger.info("Extension: Any encoding should allow the user to extend the Creator information to include custom items (0..*)")
      jsonTestCollection1.validate[OwcContext].get.creatorApplication mustEqual None
      jsonTestCollection2.validate[OwcContext].get.creatorApplication.get.title mustEqual Some("Web Enterprise Suite")
      jsonTestCollection2.validate[OwcContext].get.creatorApplication.get.uuid.isInstanceOf[UUID] mustBe true

      jsonTestCollection1.validate[OwcContext].get
        .toJson.validate[OwcContext].get.creatorApplication mustEqual None

      jsonTestCollection2.validate[OwcContext].get
        .toJson.validate[OwcContext].get.creatorApplication.get.title mustEqual Some("Web Enterprise Suite")

      val owc = jsonTestCollection2.validate[OwcContext].get
      owc.toJson.validate[OwcContext].get.creatorApplication.get.uuid mustEqual owc.creatorApplication.get.uuid
    }

    "<xz>.properties.display MAY have Properties of the display in use when the context document was created " +
      "(for display based applications only). owc:CreatorDisplay (0..1)" in {
      jsonTestCollection1.validate[OwcContext].get.creatorDisplay mustEqual None
      jsonTestCollection3.validate[OwcContext].get.creatorDisplay.get.pixelWidth mustEqual Some(800)
      jsonTestCollection3.validate[OwcContext].get.creatorDisplay.get.uuid.isInstanceOf[UUID] mustBe true

      jsonTestCollection1.validate[OwcContext].get
        .toJson.validate[OwcContext].get.creatorDisplay mustEqual None

      jsonTestCollection3.validate[OwcContext].get
        .toJson.validate[OwcContext].get.creatorDisplay.get.pixelWidth mustEqual Some(800)

      val owc = jsonTestCollection3.validate[OwcContext].get
      owc.toJson.validate[OwcContext].get.creatorDisplay.get.uuid mustEqual owc.creatorDisplay.get.uuid
    }
  }
}
