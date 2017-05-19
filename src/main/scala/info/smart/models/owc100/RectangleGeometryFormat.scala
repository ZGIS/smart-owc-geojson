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

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.context.jts.{JtsSpatialContext, JtsSpatialContextFactory}
import org.locationtech.spatial4j.exception.InvalidShapeException
import org.locationtech.spatial4j.io.ShapeIO
import org.locationtech.spatial4j.shape.Rectangle
import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

class RectangleGeometryFormat extends Format[Rectangle] with LazyLogging {

  private lazy val factory = new JtsSpatialContextFactory
  factory.geo = false
  private lazy val geoJsonWriter = jtsCtx.getFormats().getWriter(ShapeIO.GeoJSON)

  // private lazy val jtsCtx = JtsSpatialContext.GEO

  private lazy val jtsCtx = new JtsSpatialContext(factory)
  private lazy val jtsGeoJsonReader = jtsCtx.getFormats().getReader(ShapeIO.GeoJSON)

  def reads(json: JsValue): JsResult[Rectangle] = {

    json match {
      case jsVal: JsObject => parseGeoJson(jsVal.toString()) match {
        case Success(rect) => JsSuccess(rect)
        case Failure(ex) => {
          logger.error(s"JsError ValidationError error.expected.rectangle, ${ex.getLocalizedMessage}")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.rectangle"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.jsongeometry")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsongeometry"))))
      }
    }
  }

  def parseGeoJson(geoJsonString: String): Try[Rectangle] = Try {
    val shape = jtsGeoJsonReader.read(geoJsonString)

    shape match {
      case rect: Rectangle => rect
      case ex: Exception => {
        logger.error(s"Exception when parsion GeoJson: ${ex.getLocalizedMessage}")
        throw new InvalidShapeException("Couldnt parse shape", ex)
      }
      case _ => {
        logger.error(s"Invalid shape ${shape.getClass}")
        throw new InvalidShapeException(s"Invalid shape ${shape.getClass}")
      }
    }
  }

  def writes(rect: Rectangle): JsValue = {
    rectToJsValue(rect)
  }

  def rectToJsValue(rect: Rectangle): JsValue = {
    Try(Json.parse(geoJsonWriter.toString(rect))) match {
      case Success(jsValue) => jsValue
      case Failure(ex) => {
        logger.warn(s"Problem writing rectangle to Json: ${ex.getMessage}")
        // TODO decision
        Json.obj("geometry" -> JsNull)
        // Json.parse(geoJsonWriter.toString(jtsCtx.getWorldBounds))
      }
    }
  }

}
