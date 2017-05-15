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

class OwcOfferingSpec extends WordSpec with MustMatchers with LazyLogging {

  "DataType OWC:Offering GeoJSON Section 7.1.3" should {

    "<off>.code SHALL have Code identifying the requirement class identifier (URI) for the type of offering" in {

    }

    "<off>.operations[k] MAY have Array of operations used to invoke the service owc:OperationType (0..*)" in {

    }

    "<off>.contents[k] MAY have Array of contents (inline or byRef) owc:ContentType (0..*)" in {

    }

    "<off>.styles[k] MAY have Array of style sets owc:StyleSetType (0..*)" in {

    }

    "<off>.* MAY contain Any other element Extension outside of the scope of OWS Context (0..*)" in {
      logger.warn("MAY contain <off>.uuid as unique identifier")
    }
  }

  "OwcOffering GeoJson Offering Extensions" should {

    "handle WMS: http://www.opengis.net/spec/owc-geojson/1.0/req/wms" in {

    }

    "handle WFS: http://www.opengis.net/spec/owc-geojson/1.0/req/wfs" in {

    }

    "handle WCS: http://www.opengis.net/spec/owc-geojson/1.0/req/wcs" in {

    }

    "handle WPS: http://www.opengis.net/spec/owc-geojson/1.0/req/wps" in {

    }

    "not handle WMTS: http://www.opengis.net/spec/owc-geojson/1.0/req/wmts" in {

    }

    "handle CSW: http://www.opengis.net/spec/owc-geojson/1.0/req/csw" in {

    }

    "not handle GML: http://www.opengis.net/spec/owc-geojson/1.0/req/gml" in {

    }

    "not handle KML: http://www.opengis.net/spec/owc-geojson/1.0/req/kml" in {

    }

    "handle GeoTIFF: http://www.opengis.net/spec/owc-geojson/1.0/req/geotiff" in {

    }

    "not handle GMLJP2: http://www.opengis.net/spec/owc-geojson/1.0/req/gmljp2" in {

    }

    "not handle GMLCOV: http://www.opengis.net/spec/owc-geojson/1.0/req/gmlcov" in {

    }

    "define and handle SOS: http://www.opengis.net/spec/owc-geojson/1.0/req/sos" in {

    }

  }
}