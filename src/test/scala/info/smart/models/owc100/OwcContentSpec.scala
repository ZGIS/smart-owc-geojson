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

class OwcContentSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType OWC:Content GeoJSON Section 7.1.5" should {

    "<cont>.type SHALL have MIME type of the Content" in {

    }

    "<cont>.href MAY have URL of the Content (0..1)" in {

    }

    "<cont>.title MAY have Title of the Content (0..1)" in {

    }

    "<cont>.content MAY have In-line content for the Content element that can contain any text encoded media type (0..1)" in {
      logger.warn("If the 'href' attribute is present, the element content SHALL be empty. If 'href' is not provided, content SHALL be provided.")

    }

    "<cont>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <cont>.uuid as unique identifier")
    }

  }
}