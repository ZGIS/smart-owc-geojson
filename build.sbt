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

import com.sksamuel.scapegoat.sbt._
import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import scoverage.ScoverageKeys._

name := """smart-owc-geojson"""
organization := "info.smart-project"
version := "1.0.0"
scalaVersion := "2.11.8"

scalacOptions in ThisBuild ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint:_", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-unused-import", // Warn when imports are unused
  "-Ywarn-unused", // Warn when local and private vals, vars, defs, and types are unused
  "-Ywarn-numeric-widen", // Warn when numerics are widened, Int and Double, for instance
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-dead-code", // Warn when dead code is identified
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`
  "-Ywarn-nullary-override", //  Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit", // Warn when nullary methods return Unit
  "-language:reflectiveCalls",
  "-language:postfixOps" // too lazy?
)

libraryDependencies ++= {
  val playV = "2.5.12"
  val ScalaCheckV      = "1.12.5"
  val ScalaTestV       = "2.2.6"
  val scalaMockV = "3.4.2"

  Seq(
    "com.vividsolutions" % "jts-core" % "1.14.0",
    "org.locationtech.spatial4j" % "spatial4j" % "0.6",
    "org.noggit" % "noggit" % "0.7",
    "com.typesafe.play" %% "play-json" % playV,
    "com.typesafe.play" %% "play-functional" % playV,

    "uk.gov.hmrc"  %% "emailaddress" % "2.1.0",

    "com.typesafe"        % "config"           % "1.3.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "org.scalatest"       %%  "scalatest"      % ScalaTestV  % Test,
    "org.scalacheck"      %% "scalacheck"      % ScalaCheckV  % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV % Test
  )
}

resolvers += Resolver.bintrayRepo("hmrc", "releases")

// bintrayReleaseOnPublish in ThisBuild := false
publishArtifact in Test := false

bintrayRepository := "ivy2"

publishArtifact in Test := false

publishMavenStyle := false

bintrayVcsUrl := Some("https://github.com/ZGIS/smart-owc-geojson")

bintrayPackageLabels := Seq("owc", "geojson", "play", "scala")

bintrayPackage := "smart-owc-geojson"

licenses += ("Apache-2.0", url("https://opensource.org/licenses/apache-2.0"))

// Scala style task to run with tests
lazy val testScalastyle = taskKey[Unit]("testScalastyle")
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
(test in Test) <<= (test in Test) dependsOn testScalastyle

// -----------------
// coverage, style and dependency checks
enablePlugins(SiteScaladocPlugin)
// Puts Scaladoc output in `target/site/api/latest`
siteSubdirName in SiteScaladoc := "api/latest"

val genSiteDir = "src/site/generated"

includeFilter in makeSite := "*.txt" | "*.html" | "*.md" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.xml"

scapegoatVersion := "1.2.1"

scapegoatOutputPath := genSiteDir + "/scapegoat"

// scalacOptions only for the scapegoat task
scalacOptions in Scapegoat ++= Seq("-P:scapegoat:overrideLevels:TraversableHead=Warning:OptionGet=Warning")

// disabling coverage for standard tasks, call explicit in test runs / publish site
// coverageEnabled := true

lazy val coverageCopyTask = TaskKey[Unit]("copy-coverage")

coverageCopyTask := {
  println(s"Copying: ./target/scala-2.11/scoverage-report/ to $genSiteDir")
  val result = Seq("cp", "-r", "./target/scala-2.11/scoverage-report", genSiteDir + "/scoverage-report") !!
}

// sbt-dependency-graph
dependencyCheckOutputDirectory := Some(file(genSiteDir + "/dep-sec"))

// Use e.g. yEd to format the graph
dependencyGraphMLFile := file(genSiteDir + "/dep-sec/dependencies.graphml")

// Use e.g.graphviz to render
dependencyDotFile := file(genSiteDir + "/dep-sec/dependencies.dot")

// sbt-updates
dependencyUpdatesReportFile := file(genSiteDir + "/dep-sec/dependency-updates.txt")

scmInfo := Some(ScmInfo(url("https://github.com/ZGIS/smart-owc-geojson"), "git@github.com:ZGIS/smart-owc-geojson.git"))
ghpages.settings
git.remoteRepo := scmInfo.value.get.connection
