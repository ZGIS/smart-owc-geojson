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
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging

sealed trait OwcProfile {
  def value: OwcLink
}

object OwcProfile extends LazyLogging {

  case object CORE extends OwcProfile {

    val value = OwcLink(
      rel = "profile",
      href = new URL(s"$GENERIC_OWC_SPEC_URL/core"),
      mimeType = None,
      lang = None,
      title = None,
      length = None,
      uuid = UUID.randomUUID())
  }
}
