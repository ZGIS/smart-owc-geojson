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

class OwcOperationSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType OWC:Operation GeoJSON Section 7.1.4" should {

    "<op>.code SHALL have Code identifying the type of Operation" in {

    }

    "<op>.method SHALL have Code identifying the HTTP verb type of Operation. String, Example values are GET and POST." in {

    }

    "<op>.type MAY have MIME type of the expected results String, that contains a MIME media type (0..1)" in {

    }

    "<op>.href SHALL have Full Service Request URL for an HTTP GET, and request URL for HTTP POST" in {

    }

    "<op>.request MAY have Optional request body content of owc:ContentType (0..1)" in {
      logger.warn("Not necessarily XML as the content is defined by MIME-type. " +
        "If the content is text/xml or application/*+xml it SHALL be present as a XML fragment (without the <?xml... header) " +
        "and the encoding SHALL be the same as the feed.")
    }

    "<op>.result MAY have Optional Result Payload of the operation, owc:ContentType (0..1)" in {
      logger.warn("Not necessarily XML as the content is defined by MIME-type. " +
        "If the content is text/xml or application/*+xml it SHALL be present as a XML fragment (without the <?xml... header) " +
        "and the encoding SHALL be the same as the feed.")
    }

    "<op>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <op>.uuid as unique identifier")
    }
  }
}