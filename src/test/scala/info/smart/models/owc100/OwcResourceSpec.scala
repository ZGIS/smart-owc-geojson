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

class OwcResourceSpec extends WordSpec with MustMatchers with LazyLogging{

  "Class OWC:Resource GeoJson Section 7.1.2" should {

    lazy val owcResource1 = this.getClass().getResource("/owc100/owc1.geojson")
    lazy val owcResource2 = this.getClass().getResource("/owc100/owc2.geojson")
    lazy val owcResource3 = this.getClass().getResource("/owc100/owc3.geojson")

    val jsonTestCollection1 = Json.parse(scala.io.Source.fromURL(owcResource1).getLines.mkString)
    val jsonTestCollection2 = Json.parse(scala.io.Source.fromURL(owcResource2).getLines.mkString)
    val jsonTestCollection3 = Json.parse(scala.io.Source.fromURL(owcResource3).getLines.mkString)

    "<xz>.features[i].type String type that SHALL have the value 'Feature'" in {

      val jsVal = (jsonTestCollection1 \ "features")(0).get

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

      val jsRes1 = (jsonTestCollection1 \ "features")(0).get
      jsRes1.validate[OwcResource].get.isInstanceOf[OwcResource] mustBe true

    }

    "<xz>.features[i].id SHALL contain a URI value as Unambiguous reference to the identification of the Context resource (IRI)" in {

    }

    "<xz>.features[i].properties.title SHALL contain Title given to the Context resource" in {

    }

    "<xz>.features[i].properties.abstract MAY contain Account of the content of the Context resource. (0..1)" in {

    }

    "<xz>.features[i].properties.updated SHALL contain Date of the last update of the Context resource RFC-3339 date format" in {

    }

    "<xz>.features[i].properties.authors.name MAY contain Entity primarily responsible for making the content of the Context resource (0..*)" in {

    }

    "<xz>.features[i].properties.publisher MAY contain Entity responsible for making the Context resource available (0..1)" in {

    }

    "<xz>.features[i].properties.rights MAY contain rights held in and over the Context resource (0..1)" in {

    }

    "<xz>.features[i].geometry MAY contain Spatial extent or scope of the content of the Context resource, GeoJSN Geometry type (0..1)" in {

    }

    "<xz>.features[i]. properties.date MAY contain date or an interval for the Context resource, " +
      "String according to the ISO-8601 format (0..1)" in {

    }

    "<xz>.features[i].properties.links.alternates MAY contain Reference to a description of the Context resource " +
      "in alternative format, Array of link objects (0..*)" in {

    }

    "<xz>.features[i].properties.links.previews MAY contain Reference to a quick-look or browse image representing " +
      "the resource, Array of link objects, 'length' SHOULD be provided (0..*)" in {

    }

    "<xz>.features[i].properties.links.data MAY contain Reference to the location of the data resource described in the Context resource, " +
      "Array of link objects (0..*)" in {

    }

    "<xz>.features[i].properties.offering MAY contain Service or inline content offering for the resource " +
      "targeted at OGC compliant clients, owc:OfferingType (0..*)" in {

    }

    "<xz>.features[i].properties.active MAY contain Flag value indicating if the Context resource should be displayed by default, 'true' or 'false' (0..*)" in {

    }

    "<xz>.features[i].properties.links.via MAY contain Reference to a resource from which the Context resource is derived, " +
    "(e.g. source of the information) Link object (0..*)" in {
      logger.warn("the spec says, 'link object' not  'Array of link objects', example doesn NOT show an array, but spec defines multiplicity as 'Zero or more(optional)'")
    }

    "<xz>.features[i].properties.categories.term MAY contain Category related to this resource. " +
      "It MAY have a related code-list that is identified by the 'scheme' attribute (0..*)" in {

    }

    "<xz>.features[i].properties.minscaledenominator MAY contain Minimum scale for the display of the Context resource Double (0..1)" in {

    }

    "<xz>.features[i].properties.maxscaledenominator MAY contain Maximum scale for the display of the Context resource Double (0..1)" in {

    }

    "<xz>.features[i].properties.folder MAY contain Definition string of a folder name in which the resource is placed (0..1)" in {

    }

    "<xz>.features[i].properties.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.info("MAY contain <xz>.features[i].properties.uuid, but in Ows:Resource we use <xz>.features[i].id as unique identifier")
    }
  }
}
