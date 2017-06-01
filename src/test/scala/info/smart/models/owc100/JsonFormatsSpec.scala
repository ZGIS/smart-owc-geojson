/*
 * Copyright (c) 2011-2017 Interfaculty Department of Geoinformatics, University of
 * Salzburg (Z_GIS) & Institute of Geological and Nuclear Sciences Limited (GNS Science)
 * in the SMART Aquifer Characterisation (SAC) programme funded by the New Zealand
 * Ministry of Business, Innovation and Employment (MBIE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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
import org.locationtech.spatial4j.shape.Rectangle
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._

class JsonFormatsSpec extends WordSpec with MustMatchers with LazyLogging {

  "JSON MimeTypesFormat" should {

    "Reads MimeTypesFormat" in {

      val json1 = Json.parse("""{"type" : "application/gml+xml"}""")
      (json1 \ "type").validate[String](new MimeTypeFormat).get mustEqual "application/gml+xml"

      val json2 = Json.parse("""{"type" : "unknow"}""")
      (json2 \ "type").validate[String](new MimeTypeFormat).isInstanceOf[JsError] mustBe true
    }

    "Writes MimeTypesFormat" in {
      val mimeType = "application/gml+xml"
      val jsVal = Json.toJson[String](mimeType)(new MimeTypeFormat)
      jsVal.as[String] mustEqual mimeType
    }
  }

  "JSON UrlFormat" should {

    "Reads UrlFormat" in {
      val json1 = Json.parse("""{"href" : "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"}""")
      (json1 \ "href").validate[URL](new UrlFormat).get mustEqual new URL("http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1")

      val json2 = Json.parse("""{"href" : "unknow"}""")
      (json2 \ "href").validate[URL](new UrlFormat).isInstanceOf[JsError] mustBe true
    }

    "Writes UrlFormat" in {
      val url = new URL("http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1")
      val jsVal = Json.toJson[URL](url)(new UrlFormat)
      jsVal.as[String] mustEqual "http://www.someserver.com/wrs.cgi?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1"
    }
  }

  "JSON UrlFormatOfferingExtension" should {
    "Reads UrlFormatOfferingExtension" in {
      val json1 = Json.parse("""{"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/wms"}""")
      (json1 \ "code").validate[URL](new UrlFormatOfferingExtensions).get mustEqual new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")

      val json2 = Json.parse("""{"code" : "http://www.opengis.net/spec/owc-geojson/1.0/req/xxx"}""")
      (json2 \ "code").validate[URL](new UrlFormatOfferingExtensions).isInstanceOf[JsError] mustBe true
    }

    "Writes UrlFormatOfferingExtension" in {
      val code = new URL("http://www.opengis.net/spec/owc-geojson/1.0/req/wms")
      val jsVal = Json.toJson[URL](code)(new UrlFormatOfferingExtensions)
      jsVal.as[String] mustEqual "http://www.opengis.net/spec/owc-geojson/1.0/req/wms"
    }
  }

  "JSON RectangleGeometryFormat" should {

    "Reads RectangleGeometryFormat" in {

      lazy val jtsCtx = JtsSpatialContext.GEO

      val json1 =
        Json.parse(
          """{"geometry":{"type":"Polygon","coordinates":[[[164,-50],[180,-50],
            |[180,-31],[164,-31],[164,-50]]]}}""".stripMargin)

      (json1 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)

      // 500 out of lat bounds
      val json1_1 =
        Json.parse(
          """{"geometry":{"type":"Polygon","coordinates":[[[164,-50],[180,-50],
            |[180,-31],[164,-31],[164,-500]]]}}""".stripMargin)

      (json1_1 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).isInstanceOf[JsError] mustBe true

      val json1_2 =
        Json.parse(
          """{"geometry":{"type":"Polygon","coordinates":[[[-164,-50],[-90,-50],
            |[-90,-31],[-164,-31],[-164,-50]]]}}""".stripMargin)

      (json1_2 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-164.0, -90.0, -50.0, -31.0)

      // line string not a polygon
      val json3 =
        Json.parse(
          """{"geometry":{"type":"LineString","coordinates":[[-101.744384765625,39.32155002466662],
            |[-99.3218994140625,38.89530825492018],[-97.635498046875,38.87392853923629]]}}""".stripMargin)

      (json3 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).isInstanceOf[JsError] mustBe true

    }

    /**
      * RectangleGeometryFormat explicity not using GEO SpatialContext as it does "tricks"
      * for dateline cross when parsing geojsonpolygon to rectangle
      */
    "not fail on reading worldbounds RectangleGeometryFormat" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      // clockwise world (own writer produces that) (geojsonlint complains with that should be "following right hand rule")
      val json2 =
        Json.parse("""{"geometry":{"type":"Polygon","coordinates":[[[-180,-90],[-180,90],[180,90],[180,-90],[-180,-90]]]}}""".stripMargin)

      (json2 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)

      // counter clockwise (geojsonlint OK)
      val json2_1 =
        Json.parse(
          """{"geometry":{"type":"Polygon","coordinates":[[[-180,-90],[180,-90],
            |[180,90],[-180,90],[-180,-90]]]}}""".stripMargin)

      (json2_1 \ "geometry").validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)
    }

    "Writes RectangleGeometryFormat" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      val geom1 = jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)
      val jsVal1 = Json.toJson[Rectangle](geom1)(new RectangleGeometryFormat)
      jsVal1.validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)

      val geom2 = jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)
      val jsVal2 = Json.toJson[Rectangle](geom2)(new RectangleGeometryFormat)
      jsVal2.validate[Rectangle](new RectangleGeometryFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)
    }
  }

  "JSON BboxArrayFormat" should {

    "Reads BboxArrayFormat" in {

      lazy val jtsCtx = JtsSpatialContext.GEO

      val json1 =
        Json.parse(
          """{"bbox": [-180.0, -90.0, 180.0, 90.0]}""".stripMargin)

      (json1 \ "bbox").validate[Rectangle](new BboxArrayFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)

      // 500 out of lat bounds
      val json1_1 =
        Json.parse(
          """{"bbox": [164, -50,  500, -31]}""".stripMargin)

      (json1_1 \ "bbox").validate[Rectangle](new BboxArrayFormat).isInstanceOf[JsError] mustBe true

      val json1_2 =
        Json.parse(
          """{"bbox": [164, -50,  180, -31]}""".stripMargin)

      (json1_2 \ "bbox").validate[Rectangle](new BboxArrayFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)

    }

    /**
      * RectangleGeometryFormat explicity not using GEO SpatialContext as it does "tricks"
      * for dateline cross when parsing geojsonpolygon to rectangle
      */
    "not fail on reading worldbounds BboxArrayFormat" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      val json2 =
        Json.parse("""{"bbox": [-180.0, -90.0, 180.0, 90.0]}""".stripMargin)

      (json2 \ "bbox").validate[Rectangle](new BboxArrayFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(-180.0, 180.0, -90.0, 90.0)

    }

    "Writes BboxArrayFormat" in {
      lazy val jtsCtx = JtsSpatialContext.GEO

      val geom1 = jtsCtx.getShapeFactory.rect(100.0, 105.0, 0.0, 1.0)
      val jsVal1 = Json.toJson[Rectangle](geom1)(new BboxArrayFormat)
      jsVal1.validate[Rectangle](new BboxArrayFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(100.0, 105.0, 0.0, 1.0)

      val geom2 = jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)
      val jsVal2 = Json.toJson[Rectangle](geom2)(new BboxArrayFormat)
      jsVal2.validate[Rectangle](new BboxArrayFormat).get mustEqual
        jtsCtx.getShapeFactory.rect(164.0, 180.0, -50.0, -31.0)
    }
  }

  "JSON IsoLangFormat" should {

    "Reads IsoLangFormat" in {

      val json1 = Json.parse("""{"lang" : "en"}""")
      (json1 \ "lang").validate[String](new IsoLangFormat).get mustEqual "en"

      val json2 = Json.parse("""{"lang" : "unknown"}""")
      (json2 \ "lang").validate[String](new IsoLangFormat).isInstanceOf[JsError] mustBe true
    }

    "Writes IsoLangFormat" in {
      val lang = "en"
      val jsVal = Json.toJson[String](lang)(new IsoLangFormat)
      jsVal.as[String] mustEqual lang
    }
  }

  "JSON TemporalExtentFormat" should {

    "Reads TemporalExtentFormat Range" in {

      val json1 = Json.parse("""{"date": "2011-11-04T00:01:23Z/2017-12-05T17:28:56Z"}""")
      (json1 \ "date").validate[List[OffsetDateTime]](new TemporalExtentFormat).get mustEqual List(
        OffsetDateTime.of(2011, 11, 4, 0, 1, 23, 0, ZoneOffset.ofHours(0)),
        OffsetDateTime.of(2017, 12, 5, 17, 28, 56, 0, ZoneOffset.ofHours(0))
      )

      val json2 = Json.parse("""{"date" : "2013-01-02T15:24:24[UTC]"}""")
      (json2 \ "date").validate[List[OffsetDateTime]](new TemporalExtentFormat).isInstanceOf[JsError] mustBe true
    }

    "Reads TemporalExtentFormat Single" in {

      val json1 = Json.parse("""{"date" : "2013-11-02T15:24:24+12:00"}""")
      (json1 \ "date").validate[List[OffsetDateTime]](new TemporalExtentFormat).get mustEqual List(
        OffsetDateTime.of(2013, 11, 2, 15, 24, 24, 0, ZoneOffset.ofHours(12)))

      val json2 = Json.parse("""{"date" : "2013-11-02T15:24:24.446-03:30"}""")
      (json2 \ "date").validate[List[OffsetDateTime]](new TemporalExtentFormat).get mustEqual List(
        OffsetDateTime.of(2013, 11, 2, 15, 24, 24, 446 * 1000000, ZoneOffset.ofHoursMinutes(-3, -30)))
    }

    "Writes TemporalExtentFormat Range" in {
      val dateRange = List(
        OffsetDateTime.of(2011, 11, 4, 0, 1, 23, 0, ZoneOffset.ofHours(0)),
        OffsetDateTime.of(2017, 12, 5, 17, 28, 56, 0, ZoneOffset.ofHours(0))
      )

      val jsVal1 = Json.toJson[List[OffsetDateTime]](dateRange)(new TemporalExtentFormat)
      jsVal1.as[String] mustEqual "2011-11-04T00:01:23Z/2017-12-05T17:28:56Z"

    }

    "Writes TemporalExtentFormat Single" in {

      val date = List(
        OffsetDateTime.of(2013, 11, 2, 15, 24, 24, 0,  ZoneOffset.ofHours(12)))

      val jsVal = Json.toJson[List[OffsetDateTime]](date)(new TemporalExtentFormat)
      jsVal.as[String] mustEqual "2013-11-02T15:24:24+12:00"

      val date2 = List(
        OffsetDateTime.of(2013, 11, 2, 15, 24, 24, 446 * 1000000,  ZoneOffset.ofHours(12)))

      val jsVal2 = Json.toJson[List[OffsetDateTime]](date2)(new TemporalExtentFormat)
      jsVal2.as[String] mustEqual "2013-11-02T15:24:24.446+12:00"
    }
  }
}
