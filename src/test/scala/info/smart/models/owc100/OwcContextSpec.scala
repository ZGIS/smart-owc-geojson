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
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json._

class OwcContextSpec extends WordSpec with MustMatchers with LazyLogging{

  private lazy val owcResource1: URL = this.getClass().getResource("/owc100/owc1.geojson")
  private lazy val owcResource2: URL = this.getClass().getResource("/owc100/owc2.geojson")
  private lazy val owcResource3: URL = this.getClass().getResource("/owc100/owc3.geojson")

  private val jsonTestCollection1: JsValue = Json.parse(scala.io.Source.fromURL(owcResource1).getLines.mkString)
  private val jsonTestCollection2: JsValue = Json.parse(scala.io.Source.fromURL(owcResource2).getLines.mkString)
  private val jsonTestCollection3: JsValue = Json.parse(scala.io.Source.fromURL(owcResource3).getLines.mkString)

  "OwcContext GeoJson Dependency http://www.opengis.net/spec/owc/1.0/core" should {

    "comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/geojsonRules" in {
      logger.info("An OWS Context document encoded in GeoJSON (GeoJSON Context Document) " +
        "SHALL comply with the rules specified in [IETF GeoJSON]")
    }

    "comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/mime-type" in {
      logger.info("GeoJSON OWS Context documents SHALL adopt the GeoJSON MIMEtype application/vnd.geo+json")
    }

    "comply with  Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/file-extension" in {
      logger.info("GeoJSON OWS Context documents using GeoJSON SHALL use the file extension of '.geojson' or '.json'")
    }

    "comply with Requirement http://www.opengis.net/spec/owc-geojson/1.0/req/owc-encoding" in {
      logger.info("GeoJSON encoded OWS Context documents SHALL comply with the encoding rules given in the section 7.1.1.")
    }

  }

  "Class OWC:Context GeoJson Section 7.1.1" should {

    "<xz>.type SHALL have the value FeatureCollection" in {

      val fromJson: JsResult[OwcResource] = Json.fromJson[OwcResource](jsonTestCollection1)
      fromJson match {
        case JsSuccess(r: OwcResource, path: JsPath) => println("id: " + r.id)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      val result: JsResult[OwcResource] = jsonTestCollection1.validate[OwcResource]
      result match {
        case s: JsSuccess[OwcResource] => println("title: " + s.get.title)
        case e: JsError => println("Errors: " + JsError.toJson(e).toString())
      }

      val jsRes1 = (jsonTestCollection1 \ "features")(0).get
      jsRes1.validate[OwcResource].get.isInstanceOf[OwcResource] mustBe true

    }


    "<xz>.properties.links.profiles SHALL have the href value 'http://www.opengis.net/spec/owcgeojson/1.0/req/core'" in {

    }

    "<xz>.properties.lang SHALL have RFC-3066 language code" in {

    }

    "<xz>.id SHALL contain a URI value, with trailing slash" in {

    }

    "<xz>.properties.title SHALL contain Title for the Context document" in {

    }

    "<xz>.properties.subtitle MAY contain Abstract for the Context document (0..1)" in {

    }

    "<xz>.properties.updated SHALL contain Date of a creation orupdate of the Context document RFC-3339 date" in {
      logger.warn("RFC 3339 defines a profile of ISO 8601 for use in Internet protocols and standards. " +
        "It explicitly excludes durations and dates before the common era. " +
        "The more complex formats such as week numbers and ordinal days are not permitted.")

    }

    "<xz>.properties.authors.name MAY contain primarily responsible entity, string (0..*)" in {
      logger.warn("geojson objects MUST contain one or more elements on the properties.author array, " +
        "unless all of the entries of the features array contain one or more elements on the properties.authors array")

    }

    "<xz>.properties.publisher MAY contain primarily responsible entity, string (0..1)" in {

    }

    "<xz>.properties.generator MAY contain Tool/application used to create the context doc, OWC:CreatorApplication (0..1)" in {

    }

    "<xz>.properties.display MAY contain Tool/application used to create the context doc, OWC:CreatorDisplay (0..1)" in {

    }

    "<xz>.properties.rights MAY contain Information about rights held in and over the Context document (0..1)" in {

    }

    "<xz>.bbox MAY contain Geographic Area of interest of the Context document according to the GeoJSON 'bbox' definition (0..1)" in {

    }

    "<xz>.date MAY contain Date or range of dates relevant, representing a date according to the ISO-8601 format (0..1)" in {

    }

    "<xz>.properties.categories.term MAY contain keywords related to this context document. " +
      "It MAY have a related code-list that is identified by the scheme attribute (0..*)" in {

    }

    "<xz>.features MAY contain Resources available on the Context document, Features array element as defined in Section 7.1.2 (0..*)" in {

    }

    "<xz>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <xz>.uuid, but in Ows:Context we use <xz>.id as unique identifier")
    }

  }


  "DataType OWC:Creator GeoJSON Section 7.1.7" should {

    "<xz>.properties.generator MAY have The name, reference and version of the creator application " +
      "used to create the context document owc:CreatorApplication (0..1)" in {
      logger.info("Extension: Any encoding should allow the user to extend the Creator information to include custom items (0..*)")

    }

    "<xz>.properties.display MAY have Properties of the display in use when the context document was created " +
      "(for display based applications only). owc:CreatorDisplay (0..1)" in {

    }
  }

}
