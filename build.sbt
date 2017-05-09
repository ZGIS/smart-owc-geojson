name := """smart-owc-geojson"""
organization := "info.smart-project"
version := "0.9.2"
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

    "org.scalatest"       %%  "scalatest"      % ScalaTestV  % "it,test",
    "org.scalacheck"      %% "scalacheck"      % ScalaCheckV  % "it,test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings

initialCommands :=
  """|import scala.concurrent._
     |import scala.concurrent.duration._""".stripMargin

