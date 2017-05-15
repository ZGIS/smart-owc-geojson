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

class OwcStyleSetSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType OWC:StyleSet GeoJSON Section 7.1.6" should {

    "<style>.name SHALL have Unique name of the styleSet within a given offering" in {

    }

    "<style>.title SHALL have Human Readable title of the styleSet within a given offering" in {

    }

    "<style>.abstract MAY have Description of the styleSet (0..1)" in {

    }

    "<style>.default MAY declare whether this styleSet to be applied as default when the service is invoked. 'true' or 'false' (0..1)" in {

    }

    "<style>.legendURL MAY have URL of a legend image for the styleSet (0..1)" in {

    }

    "<style>.content MAY have The inline or a external reference to the styleSet definition, of owc:ContentType (0..1)" in {

    }

    "<style>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <style>.uuid as unique identifier")
    }
  }
}