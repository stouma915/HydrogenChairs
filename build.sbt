ThisBuild / version := "1.0.0"
ThisBuild / description := "PaperMC用椅子プラグイン"
ThisBuild / scalaVersion := "3.1.1"

resolvers ++= Seq(
  "papermc.io" at "https://papermc.io/repo/repository/maven-public/"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.3.6",
  "io.papermc.paper" % "paper-api" % "1.18.1-R0.1-SNAPSHOT"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _ @_*) =>
    MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith ".properties" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" =>
    MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

lazy val root = (project in file("."))
  .settings(
    name := "HydrogenChairs",
    assemblyOutputPath in assembly := baseDirectory.value / "target" / "build" / s"${name.value}-${version.value}.jar"
  )
