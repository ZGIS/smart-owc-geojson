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
import org.scalatest.{Matchers, MustMatchers, WordSpec}

class OwcCategorySpec extends WordSpec with MustMatchers with LazyLogging{

  "GeoJSON Category aka keywords" should {

    "<props>.categories.term SHALL have a keyword related to the context document or resource" in {

    }

    "<props>.categories.scheme MAY have a related code-list that is identified by the scheme attribute (0..1)" in {

    }

    "<props>.categories.label MAY have a speaking label in relation to the keyword term (0..1)" in {

    }

  }
}