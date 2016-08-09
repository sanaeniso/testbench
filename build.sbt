name := "TestBench"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  ws,
  cache,
  specs2 % Test,
  evolutions
)

libraryDependencies ++= dataBaseDependencies

libraryDependencies ++= mysqlDependencies

lazy val dataBaseDependencies = Seq (
  "com.typesafe.slick" %% "slick" % "3.1.0",

  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0"
)

lazy val mysqlDependencies = Seq(
  "mysql" % "mysql-connector-java" % "5.1.34"
)

lazy val h2Dependencies = Seq(
  "com.h2database" % "h2" % "1.4.185"
)

lazy val otherDependencies = Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.0-rc2"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
