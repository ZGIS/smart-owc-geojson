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

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._

class OwcResourceSpec extends WordSpec with MustMatchers with LazyLogging {

  private lazy val owcResource1: URL = this.getClass().getResource("/owc100/owc1.geojson")
  private lazy val owcResource2: URL = this.getClass().getResource("/owc100/owc2.geojson")
  private lazy val owcResource3: URL = this.getClass().getResource("/owc100/owc3.geojson")

  private val jsonTestCollection1: JsValue = Json.parse(scala.io.Source.fromURL(owcResource1).getLines.mkString)
  private val jsonTestCollection2: JsValue = Json.parse(scala.io.Source.fromURL(owcResource2).getLines.mkString)
  private val jsonTestCollection3: JsValue = Json.parse(scala.io.Source.fromURL(owcResource3).getLines.mkString)

  "Class OWC:Resource GeoJson Section 7.1.2" should {

    "<xz>.features[i].type String type that SHALL have the value 'Feature'" in {

      val jsVal = (jsonTestCollection3 \ "features") (0).get

      val fromJson: JsResult[OwcResource] = Json.fromJson[OwcResource](jsVal)
      fromJson match {
        case JsSuccess(r: OwcResource, path: JsPath) => println("id: " + r.id)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcResource] = jsVal.validate[OwcResource]
      result match {
        case s: JsSuccess[OwcResource] => println("title: " + s.get.title)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.isInstanceOf[OwcResource] mustBe true
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.isInstanceOf[OwcResource] mustBe true
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.isInstanceOf[OwcResource] mustBe true

    }

    "<xz>.features[i].id SHALL contain a URI value as Unambiguous reference to the identification of the Context resource (IRI)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.id.isInstanceOf[URL] mustBe true
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.id mustEqual new URL("http://ows-9.compusult.net/wes/serviceManagerCSW/csw/http://ows-9.compusult.net/wes/serviceManagerCSW/csw/9496276a-4f6e-47c1-94bb-f604245fac57/")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.id mustEqual new URL("http://portal.smart-project.info/context/smart-sac_add-nz-dtm-100x100/")
    }

    "<xz>.features[i].properties.title SHALL contain Title given to the Context resource" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.title mustEqual "WPS 52 north"
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.title mustEqual "gml:AbstractFeature"
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.title mustEqual "NZ DTM 100x100"
    }

    "<xz>.features[i].properties.abstract MAY contain Account of the content of the Context resource. (0..1)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.subtitle mustEqual Some("abstract about data")
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.subtitle mustEqual None
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.subtitle mustEqual Some("New Zealand Digital Terrain Model 100m by 100m resolution")
    }

    "<xz>.features[i].properties.updated SHALL contain Date of the last update of the Context resource RFC-3339 date format" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.updateDate mustEqual OffsetDateTime.of(2013, 5, 19, 0, 0, 0, 0,  ZoneOffset.ofHours(0))
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.updateDate mustEqual OffsetDateTime.of(2013, 1, 2, 15, 24, 24, 446 * 1000000, ZoneOffset.ofHoursMinutes(-3, -30))
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.updateDate mustEqual OffsetDateTime.of(2016, 2, 21, 11, 58, 23, 0,  ZoneOffset.ofHours(0))
    }

    "<xz>.features[i].properties.authors.name MAY contain Entity primarily responsible for making the content of the Context resource (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.author mustEqual List()
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.author.head.name mustEqual Some("interactive-instruments")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.author.isEmpty mustBe true
    }

    "<xz>.features[i].properties.publisher MAY contain Entity responsible for making the Context resource available (0..1)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.publisher mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.publisher mustEqual None
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.publisher mustEqual Some("GNS Science and Salzburg Uni ZGIS Dept")
    }

    "<xz>.features[i].properties.rights MAY contain rights held in and over the Context resource (0..1)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.rights mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.rights mustEqual Some("Copyright (c) 2012. Some rights reserved. This feed licensed under a Creative Commons Attribution 3.0 License.")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.rights mustEqual None
    }

    "<xz>.features[i].geometry MAY contain Spatial extent or scope of the content of the Context resource, GeoJSON Geometry type (0..1)" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.geospatialExtent mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.geospatialExtent mustEqual
        Some(jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0))
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.geospatialExtent mustEqual
        Some(jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0))
    }

    "<xz>.features[i].properties.date MAY contain date or an interval for the Context resource, " +
      "String according to the ISO-8601 format (0..1)" in {

      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.temporalExtent mustEqual Some(List(
        OffsetDateTime.of(2013, 11, 2, 15, 24, 24, 446 * 1000000,  ZoneOffset.ofHours(12))))
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.temporalExtent mustEqual None
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.temporalExtent mustEqual Some(List(
        OffsetDateTime.of(2011, 11, 4, 0, 1, 23, 0, ZoneOffset.ofHours(0)),
        OffsetDateTime.of(2017, 12, 5, 17, 28, 56, 0, ZoneOffset.ofHours(0))
      ))

    }

    "<xz>.features[i].properties.links.alternates MAY contain Reference to a description of the Context resource " +
      "in alternative format, Array of link objects (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.contentDescription mustEqual List()
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.contentDescription.head.mimeType mustEqual Some("text/html")
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.contentDescription.head.href.isInstanceOf[URL] mustBe true
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.contentDescription.isEmpty mustBe true
    }

    "<xz>.features[i].properties.links.previews MAY contain Reference to a quick-look or browse image representing " +
      "the resource, Array of link objects, 'length' SHOULD be provided (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.preview mustEqual List()
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.preview.isEmpty mustBe true
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.preview.head.mimeType mustEqual Some("image/png")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.preview.head.href.isInstanceOf[URL] mustBe true
    }

    "<xz>.features[i].properties.links.data MAY contain Reference to the location of the data resource described in the Context resource, " +
      "Array of link objects (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.contentByRef.length mustEqual 1
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.contentByRef.head.mimeType mustEqual Some("application/x-hdf5")
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.contentByRef.head.length mustEqual Some(453123432)
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.contentByRef.isEmpty mustBe true
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.contentByRef.isEmpty mustBe true

    }

    "<xz>.features[i].properties.offering MAY contain Service or inline content offering for the resource " +
      "targeted at OGC compliant clients, owc:OfferingType (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.offering.length mustEqual 1
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.offering.length mustEqual 2
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.offering.length mustEqual 2
    }

    "<xz>.features[i].properties.active MAY contain Flag value indicating if the Context resource should be displayed by default, 'true' or 'false' (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.active mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.active mustEqual Some(false)
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.active mustEqual Some(true)
    }

    "<xz>.features[i].properties.links.via MAY contain Reference to a resource from which the Context resource is derived, " +
      "(e.g. source of the information) Link object (0..*)" in {
      logger.warn("the spec says, 'link object' not  'Array of link objects', example doesn NOT show an array, but spec defines multiplicity as 'Zero or more(optional)'")
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.resourceMetadata.length mustEqual 1
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.resourceMetadata.head.mimeType mustEqual Some("application/xml")
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.resourceMetadata.head.length mustEqual Some(435)
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.resourceMetadata.head.lang mustEqual Some("es")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.resourceMetadata.isEmpty mustBe true
    }

    "<xz>.features[i].properties.categories.term MAY contain Category related to this resource. " +
      "It MAY have a related code-list that is identified by the 'scheme' attribute (0..*)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.keyword.isEmpty mustBe true
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.keyword.length mustEqual 1
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.keyword.head.term mustEqual "GEOSSDataCore"
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.keyword.head.scheme mustEqual Some("view-groups")
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.keyword.head.label mustEqual Some("Informative Layers")
    }

    "<xz>.features[i].properties.minscaledenominator MAY contain Minimum scale for the display of the Context resource Double (0..1)" in {

      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.minScaleDenominator mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.minScaleDenominator.get mustEqual 100.0
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.minScaleDenominator mustEqual None
    }

    "<xz>.features[i].properties.maxscaledenominator MAY contain Maximum scale for the display of the Context resource Double (0..1)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.maxScaleDenominator mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.maxScaleDenominator.get mustEqual 1000000.0
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.maxScaleDenominator mustEqual None

    }

    "<xz>.features[i].properties.folder MAY contain Definition string of a folder name in which the resource is placed (0..1)" in {
      (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get.folder mustEqual None
      (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get.folder mustEqual None
      (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get.folder  mustEqual Some("/view-groups/sac_add")

    }

    "Not used: <xz>.features[i].properties.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("Not used: MAY contain <xz>.features[i].properties.uuid, but in Ows:Resource we use <xz>.features[i].id as unique identifier")
    }
  }

  "OwcResource Writes" should {

    val res1 = (jsonTestCollection1 \ "features") (0).get.validate[OwcResource].get
    val res2 = (jsonTestCollection2 \ "features") (0).get.validate[OwcResource].get
    val res3 = (jsonTestCollection3 \ "features") (0).get.validate[OwcResource].get

    "write OwcResource GeoJSON" in {

      res1.toJson.validate[OwcResource].get mustEqual res1
      res2.toJson.validate[OwcResource].get mustEqual res2
      res3.toJson.validate[OwcResource].get mustEqual res3
    }
  }
}
