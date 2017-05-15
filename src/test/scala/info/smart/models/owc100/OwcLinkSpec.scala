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

class OwcLinkSpec extends WordSpec with MustMatchers with LazyLogging{


  "DataType 'links' GeoJSON Section 7.1.10" should {

    "<xz>.href SHALL have URI describing the related resource" in {

    }

    "<xz>.type MAY have media type of the representation that is expected  when the value of the href attribute is dereferenced (0..1" in {

    }

    "<xz>.lang MAY have Language of the resource pointed to by the href attribute, an RFC-3066 code (0..1)" in {

    }

    "<xz>.title MAY have Human-readable information about the link (0..1)" in {

    }

    "<xz>.length MAY have expected content length in octets of the representation returned " +
      "when the URI in the href attribute is dereferenced. non-negative Integer (0..1)" in {

    }

    "<xz>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <xz>.uuid as unique identifier")
    }
  }
}