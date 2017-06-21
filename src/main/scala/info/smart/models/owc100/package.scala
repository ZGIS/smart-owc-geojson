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

package info.smart.models

import java.net.URL

package object owc100 {

  // This is for everybody
  val GEOJSON_OWC_SPEC_URL = "http://www.opengis.net/spec/owc-geojson/1.0/req"
  val    ATOM_OWC_SPEC_URL = "http://www.opengis.net/spec/owc-atom/1.0/req"
  val GENERIC_OWC_SPEC_URL = "http://www.opengis.net/spec/owc-generic/1.0/req"

  val SUPPORTED_GEOJSON_OFFERING_EXTENSIONS: List[URL] =
    List(
      new URL(s"$GEOJSON_OWC_SPEC_URL/wms"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/wfs"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/wcs"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/wps"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/csw"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/geotiff"),
      new URL(s"$GEOJSON_OWC_SPEC_URL/sos")
    )

  val SUPPORTED_GEOJSON_PROFILES: List[URL] =
    List(
      new URL(s"$GEOJSON_OWC_SPEC_URL/core")
    )

  def geojsonSpecUrlToGenericSpecUrl(geojsonSpecUrl: URL) : URL = {
    val specCode = "/" + geojsonSpecUrl.getPath.split("/").last.trim
    new URL(s"$GENERIC_OWC_SPEC_URL$specCode")
  }

  def genericSpecUrlToGeojsonSpecUrl(genericSpecUrl: URL) : URL = {
    val specCode = genericSpecUrl.getPath.split("/").last.trim
    new URL(s"$GEOJSON_OWC_SPEC_URL/$specCode")
  }
}
