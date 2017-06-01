package info.smart.models.owc100

import com.typesafe.scalalogging.LazyLogging
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.locationtech.spatial4j.shape.Rectangle
import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

class BboxArrayFormat extends Format[Rectangle] with LazyLogging {

  // private lazy val factory = new JtsSpatialContextFactory
  // factory.geo = false
  private lazy val jtsCtx = JtsSpatialContext.GEO
  // private lazy val jtsCtx = new JtsSpatialContext(factory)

  def parseBboxArray(bbox: List[Double]): Try[Rectangle] = Try {
    jtsCtx.getShapeFactory.rect(bbox(0), bbox(2), bbox(1), bbox(3))
  }

  def reads(json: JsValue): JsResult[Rectangle] = {

    json match {
      case jsVal: JsArray => parseBboxArray(jsVal.as[List[Double]]) match {
        case Success(rect) => JsSuccess(rect)
        case Failure(ex) => {
          logger.error(s"JsError ValidationError error.expected.bbox, ${ex.getLocalizedMessage}")
          JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.bbox"))))
        }
      }
      case _ => {
        logger.error("JsError ValidationError error.expected.jsarray[double]")
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsarray[double]"))))
      }
    }
  }

  def writes(rect: Rectangle): JsValue = {

    Try(Json.toJson(Array(
      rect.getMinX,
      rect.getMinY,
      rect.getMaxX,
      rect.getMaxY
    ))) match {
      case Success(jsValue) => jsValue
      case Failure(ex) => {
        logger.warn(s"Problem writing bbox to Json: ${ex.getMessage}")
        // TODO decision
        Json.obj("bbox" -> JsNull)
        // Json.parse(geoJsonWriter.toString(jtsCtx.getWorldBounds))
      }
    }
  }
}
