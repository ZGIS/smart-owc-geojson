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

class OwcCreatorSpec extends WordSpec with MustMatchers with LazyLogging{

  "DataType OWC:Creator GeoJSON Section 7.1.7" should {

    "<xz>.properties.generator MAY have The name, reference and version of the creator application " +
      "used to create the context document owc:CreatorApplication (0..1)" in {
      logger.warn("Extension: Any encoding should allow the user to extend the Creator information to include custom items (0..*)")

    }

    "<xz>.properties.display MAY have Properties of the display in use when the context document was created " +
      "(for display based applications only). owc:CreatorDisplay (0..1)" in {

    }
  }


  "DataType OWC:CreatorApplication GeoJSON Section 7.1.8" should {

    "<xz>.properties.generator.title MAY have Title or name of the application (0..1)" in {

    }

    "<xz>.properties.generator.uri MAY have URI describing the creator application, that is relevant to the client (web address). (0..1)" in {

    }

    "<xz>.properties.generator. version MAY have Version of the creator application (0..1)" in {

    }
  }


  "DataType OWC:CreatorDisplay GeoJSON Section 7.1.9" should {

    "<xz>.properties.display.pixelWidth MAY have Width measured in pixels of the display showing the Area of Interest, Positive Integer (0..1)" in {

    }

    "<xz>.properties.display.pixelHeight MAY have Width measured in pixels of the display showing the Area of Interest, Positive Integer (0..1)" in {

    }

    "<xz>.properties.display.mmPerPixel MAY have The size of a pixel of the display in milimeters (to allow for the real display size to be calculated), Double (0..1)" in {

    }

    "<xz>.properties.display.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <xz>.properties.display.uuid as unique identifier")
    }
  }
}