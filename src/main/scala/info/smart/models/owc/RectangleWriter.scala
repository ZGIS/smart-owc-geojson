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

package info.smart.models.owc

import org.locationtech.spatial4j.context.SpatialContext
import org.locationtech.spatial4j.io.ShapeIO
import org.locationtech.spatial4j.shape.Rectangle
import play.api.libs.json.{ JsValue, Json, Writes }

import scala.util.{ Failure, Success, Try }

/**
 * RectangleWriter
 */
class RectangleWriter extends Writes[Rectangle] with ClassnameLogger {

  private lazy val ctx = SpatialContext.GEO
  private lazy val geoJsonWriter = ctx.getFormats().getWriter(ShapeIO.GeoJSON)

  def rectToJsValue(rect: Rectangle): JsValue = {
    Try(Json.parse(geoJsonWriter.toString(rect))) match {
      case Success(jsValue) => jsValue
      case Failure(ex) => {
        logger.warn(s"Problem writing rectangle to Json: ${ex.getMessage}")
        Json.parse(geoJsonWriter.toString(ctx.getWorldBounds))
      }
    }
  }

  def writes(rect: Rectangle): JsValue = {
    rectToJsValue(rect)
  }

}
