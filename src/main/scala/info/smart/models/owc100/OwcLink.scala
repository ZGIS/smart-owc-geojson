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
  * OwcLink in most cases will have anarray of links under the path of the rel
  * where the super-ordinate rel is one of "self", "profile", "icon", "previews", "via", "data" or "alternates"
  * + href :URI
  * + type :String [0..1]
  * + lang :String [0..1]
  * + title :String [0..1]
  * + length :Integer [0..1]
  * + extension :Any [0..*]
  */
case class OwcLink(
                    href: URL,
                    mimeType: Option[String],
                    lang: Option[String],
                    title: Option[String],
                    length: Option[Int],
                    rel: String, // can at least stay here via extension :-)
                    uuid: UUID
                  ) extends LazyLogging {

}

object OwcLink extends LazyLogging {

}
