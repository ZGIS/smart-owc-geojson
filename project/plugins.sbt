
resolvers ++= Seq(
  Classpaths.sbtPluginReleases,
  Resolver.jcenterRepo,
  "Typesafe Repo"           at "http://repo.typesafe.com/typesafe/releases/",
  "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"
)

// https://github.com/sbt/sbt-bintray/issues/104
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.3.0")

// addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.2")

// addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")

addSbtPlugin("com.scalapenos" % "sbt-prompt" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.5")

// code quality etc documentation plugins
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

// dependencies and security
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "0.1.7")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")
