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
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.locationtech.spatial4j.io.ShapeIO
import org.locationtech.spatial4j.shape.Rectangle
import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.{ Failure, Success, Try }

/**
 * RectangleReader
 */
class RectangleReader extends Reads[Rectangle] with ClassnameLogger {

  private lazy val ctx = SpatialContext.GEO
  private lazy val jtsCtx = JtsSpatialContext.GEO
  private lazy val jtsGeoJsonReader = jtsCtx.getFormats().getReader(ShapeIO.GeoJSON)

  /**
   *
   * @param geoJsonString
   * @return
   */
  def jsToRect(geoJsonString: String): Option[Rectangle] = {
    Try(jtsGeoJsonReader.read(geoJsonString).asInstanceOf[Rectangle]) match {
      case Success(rect: Rectangle) => Some(rect)
      case Failure(ex) => {
        logger.warn(s"Problem reading geoJsonString to rectangle: ${ex.getMessage}")
        None
      }
    }
  }

  /**
   *
   * @param geoJsonJsValue
   * @return
   */
  def jsToRect(geoJsonJsValue: JsValue): Option[Rectangle] = {
    val geoJsonString = Json.stringify(geoJsonJsValue)
    jsToRect(geoJsonString)
  }

  /**
   *
   * @param json
   * @return
   */
  def reads(json: JsValue): JsResult[Rectangle] = {

    logger.warn(s"trying js to rect ${Json.stringify(json)}")
    val rectOpt = jsToRect(json)

    rectOpt match {
      case Some(s) => {
        JsSuccess(s)
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.geometry")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.rectangle"))))
      }
    }
  }
}
