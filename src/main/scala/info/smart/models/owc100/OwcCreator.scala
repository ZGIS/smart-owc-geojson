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

/**
  * + creatorApplication :CreatorApplication [0..1]
  * + creatorDisplay :CreatorDisplay [0..1]
  * + extension :Any [0..*]
  */
case class OwcCreator(
                       creatorApplication: Option[OwcCreatorApplication],
                       creatorDisplay: Option[OwcCreatorDisplay],
                       uuid: UUID
                     ) extends LazyLogging {

}

object OwcCreator extends LazyLogging {

}

/*
CreatorApplication
+ title :CharacterString [0..1]
+ uri :URI [0..1]
+ version :Version [0..1]
+ the single only class that doesn't have explicit extension???
*/

case class OwcCreatorApplication(
                                  title: Option[String],
                                  uri: Option[URL],
                                  version: Option[String],
                                  uuid: UUID
                                ) extends LazyLogging {

}

object OwcCreatorApplication extends LazyLogging {

}

/*
CreatorDisplay
+ pixelWidth :int [0..1]
+ pixelHeight :int [0..1]
+ mmPerPixel :double [0..1]
+ extension :Any [0..*]
 */

case class OwcCreatorDisplay(
                              pixelWidth: Option[Int],
                              pixelHeight: Option[Int],
                              mmPerPixel: Option[Double],
                              uuid: UUID
                            ) extends LazyLogging {

}

object OwcCreatorDisplay extends LazyLogging {

}