name := """smart-owc-geojson"""
organization := "info.smart-project"
version := "0.9.3-SNAPSHOT"
scalaVersion := "2.11.8"

scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls"
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

    "com.typesafe"        % "config"           % "1.3.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",

    "org.scalatest"       %%  "scalatest"      % ScalaTestV  % Test,
    "org.scalacheck"      %% "scalacheck"      % ScalaCheckV  % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV % Test
  )
}

// bintrayReleaseOnPublish in ThisBuild := false

publishArtifact in Test := false

bintrayRepository := "ivy2"

publishArtifact in Test := false

publishMavenStyle := false

bintrayVcsUrl := Some("https://github.com/ZGIS/smart-owc-geojson")

bintrayPackageLabels := Seq("owc", "geojson", "play", "scala")

bintrayPackage := "smart-owc-geojson"

licenses += ("Apache-2.0", url("https://opensource.org/licenses/apache-2.0"))


