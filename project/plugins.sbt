
resolvers ++= Seq(
  Classpaths.sbtPluginReleases,
  Resolver.jcenterRepo,
  "Typesafe Repo"           at "http://repo.typesafe.com/typesafe/releases/",
  "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"
)

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.4.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.2")

addSbtPlugin("com.scalapenos" % "sbt-prompt" % "1.0.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.5")