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

/**
  + id :CharacterString
  + title :CharacterString
  + abstract :CharacterString [0..1]
  + updateDate :TM_Date [0..1]
  + author :CharacterString [0..*]
  + publisher :CharacterString [0..1]
  + rights :CharacterString [0..1]
  + geospatialExtent :GM_Envelope [0..1]
  + temporalExtent :TM_GeometricPrimitive [0..1]
  + contentDescription :Any [0..1]
  + preview :URI [0..*]
  + contentByRef :URI [0..*]
  + offering :Offering [0..*]
  + active :Boolean [0..1]
  + keyword :CharacterString [0..*]
  + maxScaleDenominator :double [0..1]
  + minScaleDenominator :double [0..1]
  + folder :CharacterString [0..1]
  + extension :Any [0..*]
  */
class OwcResource {

}
