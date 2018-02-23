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

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{GivenWhenThen, MustMatchers, WordSpec}
import play.api.libs.json._

class RegressionTests1 extends WordSpec with MustMatchers with GivenWhenThen with LazyLogging {

  private lazy val awahouResource: URL = this.getClass().getResource("/owc100/awahou-catchment.json")
  private lazy val newzealandResource: URL = this.getClass().getResource("/owc100/newzealand-overview.json")
  private lazy val horowhenuaResource: URL = this.getClass().getResource("/owc100/horowhenua.json")
  private lazy val ngongotahaResource: URL = this.getClass().getResource("/owc100/ngongotaha.json")
  private lazy val saccasestudiesResource: URL = this.getClass().getResource("/owc100/sac-casestudies.json")

  "Regression Tests for bigger collections" when {

    val jsonAwahou: JsValue = Json.parse(scala.io.Source.fromURL(awahouResource).getLines.mkString)
    val jsonNewzealand: JsValue = Json.parse(scala.io.Source.fromURL(newzealandResource).getLines.mkString)
    val jsonHorowhenua: JsValue = Json.parse(scala.io.Source.fromURL(horowhenuaResource).getLines.mkString)
    val jsonNogongotaha: JsValue = Json.parse(scala.io.Source.fromURL(ngongotahaResource).getLines.mkString)
    val jsonSaccasestudies: JsValue = Json.parse(scala.io.Source.fromURL(saccasestudiesResource).getLines.mkString)

    "load from resource" in {

      Then("jsonAwahou must be instance Of[JsValue]")
      jsonAwahou.isInstanceOf[JsValue] mustBe true
      Then("jsonNewzealand must be instance Of[JsValue]")
      jsonNewzealand.isInstanceOf[JsValue] mustBe true
      Then("jsonHorowhenua must be instance Of[JsValue]")
      jsonHorowhenua.isInstanceOf[JsValue] mustBe true
      Then("jsonNogongotaha must be instance Of[JsValue]")
      jsonNogongotaha.isInstanceOf[JsValue] mustBe true
      Then("jsonSaccasestudies must be instance Of[JsValue]")
      jsonSaccasestudies.isInstanceOf[JsValue] mustBe true
    }

    "validate as [OwcContext]" in {
      Then("jsonAwahou must validate as [OwcContext]")
      jsonAwahou.validate[OwcContext].isSuccess mustBe true
      Then("jsonNewzealand must validate as [OwcContext]")
      jsonNewzealand.validate[OwcContext].isSuccess mustBe true
      Then("jsonHorowhenua must validate as [OwcContext]")
      jsonHorowhenua.validate[OwcContext].isSuccess mustBe true
      Then("jsonNogongotaha must validate as [OwcContext]")
      jsonNogongotaha.validate[OwcContext].isSuccess mustBe true
      Then("jsonSaccasestudies must validate as [OwcContext]")
      jsonSaccasestudies.validate[OwcContext].isSuccess mustBe true
    }

    "parse the demo feature" in {
      val demoResourceText = """{
                               |      "type": "Feature",
                               |      "id": "https://portal.smart-project.info/context/resource/3a1c7634-817e-4442-9cd8-0eeaefdd0f89",
                               |      "geometry": {
                               |          "type": "Polygon",
                               |          "coordinates": [
                               |            [
                               |              [
                               |                176.1,
                               |                -38.11
                               |              ],
                               |              [
                               |                176.3,
                               |                -38.11
                               |              ],
                               |              [
                               |                176.3,
                               |                -37.97
                               |              ],
                               |              [
                               |                176.1,
                               |                -37.97
                               |              ],
                               |              [
                               |                176.1,
                               |                -38.11
                               |              ]
                               |            ]
                               |          ]
                               |        },
                               |      "properties": {
                               |        "title": "horowhenua_ws:Rotorua_subcatchments",
                               |        "abstract": "Lake Rotorua Sub Catchments (June 2014)",
                               |        "updated": "2018-02-16T04:14:56.884Z",
                               |        "authors": [
                               |          {
                               |            "name": "Alex Kmoch",
                               |            "email": "allixender@gmail.com",
                               |            "uri": "https://portal.smart-project.info"
                               |          }
                               |        ],
                               |        "publisher": "GNS Science",
                               |        "rights": "CC BY SA 4.0 NZ",
                               |        "links": {
                               |          "alternates": [],
                               |          "previews": [
                               |            {
                               |              "href": "https://portal.smart-project.info/fs/images/nz_m.png",
                               |              "type": "image/png",
                               |              "rel": "icon"
                               |            },
                               |            {
                               |              "href": "https://portal.smart-project.info/geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&LAYER=horowhenua_ws:Rotorua_subcatchments",
                               |              "type": "image/png",
                               |              "rel": "icon"
                               |            }
                               |          ],
                               |          "data": [
                               |            {
                               |              "href": "https://portal.smart-project.info/geoserver/wms",
                               |              "type": "application/xml",
                               |              "title": "Lake Rotorua Sub Catchments (June 2014):horowhenua_ws:Rotorua_subcatchments"
                               |            }
                               |          ],
                               |          "via": [
                               |            {
                               |              "href": "https://portal.smart-project.info/context/resource/3a1c7634-817e-4442-9cd8-0eeaefdd0f89",
                               |              "rel": "via"
                               |            }
                               |          ]
                               |        },
                               |        "offerings": [
                               |          {
                               |            "code": "http://www.opengis.net/spec/owc-geojson/1.0/req/wms",
                               |            "operations": [
                               |              {
                               |                "code": "GetCapabilities",
                               |                "method": "GET",
                               |                "type": "application/xml",
                               |                "href": "https://portal.smart-project.info/geoserver/wms?VERSION=1.3.0&REQUEST=GetCapabilities"
                               |              },
                               |              {
                               |                "code": "GetMap",
                               |                "method": "GET",
                               |                "type": "image/png",
                               |                "href": "https://portal.smart-project.info/geoserver/wms?VERSION=1.3&REQUEST=GetMap&SRS=EPSG:4326&BBOX=176.1,-38.11,176.3,-37.97&WIDTH=800&HEIGHT=600&LAYERS=horowhenua_ws:Rotorua_subcatchments&FORMAT=image/png&TRANSPARENT=TRUE&EXCEPTIONS=application/vnd.ogc.se_xml"
                               |              }
                               |            ],
                               |            "contents": [],
                               |            "styles": []
                               |          },
                               |          {
                               |            "code": "http://www.opengis.net/spec/owc-geojson/1.0/req/csw",
                               |            "operations": [
                               |              {
                               |                "code": "GetCapabilities",
                               |                "method": "GET",
                               |                "type": "application/xml",
                               |                "href": "https://portal.smart-project.info/pycsw/csw?SERVICE=CSW&VERSION=2.0.2&REQUEST=GetCapabilities"
                               |              },
                               |              {
                               |                "code": "GetRecordById",
                               |                "method": "POST",
                               |                "type": "application/xml",
                               |                "href": "https://portal.smart-project.info/journalcsw/journalcsw?request=GetRecordById&version=2.0.2&service=CSW&elementSetName=full&outputSchema=http%3A%2F%2Fwww.isotc211.org%2F2005%2Fgmd&Id="
                               |              }
                               |            ],
                               |            "contents": []
                               |          }
                               |        ],
                               |        "categories": [
                               |          {
                               |            "scheme": "view-groups",
                               |            "term": "awa_catch",
                               |            "label": "Awahou Catchment"
                               |          }
                               |        ],
                               |        "active": false,
                               |        "folder": "awa_catch"
                               |      }
                               |    }""".stripMargin

      Then("parse the demo feature as instance of JsValue")
      Json.parse(demoResourceText).isInstanceOf[JsValue] mustBe true
      Then("validate the demo feature as OwcResource")
      Json.parse(demoResourceText).validate[OwcResource].isSuccess mustBe true
      Then("count elements offerings")
      Json.parse(demoResourceText).validate[OwcResource].asOpt.map(_.offering.length mustBe 2)
      Then("check geometry")
      Json.parse(demoResourceText).validate[OwcResource].asOpt.map(_.geospatialExtent mustBe defined)
    }

    "have all the features" in {

      Then("jsonAwahou awahou must have 17 owcResources")
      jsonAwahou.validate[OwcContext].asOpt.map(o => o.resource.length mustBe 17)
      jsonAwahou.validate[OwcContext].fold(
        invalid => {
          println(invalid.map(pe => pe._1.toJsonString + " " + pe._2.map(v =>v.message).mkString("/")).mkString(" :: "))
          false
        },
        valid =>
          true) mustBe true

      Then("jsonNewzealand newzealand must have 13 owcResources")
      // jsonNewzealand.validate[OwcContext].asOpt.map(o => o.resource.foreach(r => println(r.id)))
      jsonNewzealand.validate[OwcContext].asOpt.map(o => o.resource.length mustBe 13)
      jsonNewzealand.validate[OwcContext].asOpt.map(o => o.resource.count(_.geospatialExtent.isDefined) mustBe 13)

      Then("jsonHorowhenua horowhenua must have 27 owcResources")
      jsonHorowhenua.validate[OwcContext].asOpt.map(o => o.resource.length mustBe 27)
      jsonHorowhenua.validate[OwcContext].asOpt.map(o => o.resource.count(_.geospatialExtent.isDefined) mustBe 27)

      Then("jsonNogongotaha ngongotaha must have 7 owcResources")
      jsonNogongotaha.validate[OwcContext].asOpt.map(o => o.resource.length mustBe 7)
      jsonNogongotaha.validate[OwcContext].asOpt.map(o => o.resource.count(_.geospatialExtent.isDefined) mustBe 7)

      Then("jsonSaccasestudies sac-casestudies must have 15 owcResources")
      jsonSaccasestudies.validate[OwcContext].asOpt.map(o => o.resource.length mustBe 15)
      jsonSaccasestudies.validate[OwcContext].asOpt.map(o => o.resource.count(_.geospatialExtent.isDefined) mustBe 15)

    }

    "OwcContext have geometry" in {
      Then("jsonAwahou must have geometry as [OwcContext]")
      jsonAwahou.validate[OwcContext].asOpt.map(o => o.areaOfInterest.isDefined mustBe true)
      Then("jsonNewzealand must have geometry as [OwcContext]")
      jsonNewzealand.validate[OwcContext].asOpt.map(o => o.areaOfInterest.isDefined mustBe true)
      Then("jsonHorowhenua must have geometry as [OwcContext]")
      jsonHorowhenua.validate[OwcContext].asOpt.map(o => o.areaOfInterest.isDefined mustBe true)
      Then("jsonNogongotaha must have geometry as [OwcContext]")
      jsonNogongotaha.validate[OwcContext].asOpt.map(o => o.areaOfInterest.isDefined mustBe true)
      Then("jsonSaccasestudies must have geometry as [OwcContext]")
      jsonSaccasestudies.validate[OwcContext].asOpt.map(o => o.areaOfInterest.isDefined mustBe true)
    }
  }
}
