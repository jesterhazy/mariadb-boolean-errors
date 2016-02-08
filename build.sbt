name := """mariadb-boolean-errors"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.5.0",
//  "org.mariadb.jdbc" % "mariadb-java-client" % "1.3.4"
  "mysql" % "mysql-connector-java" % "5.1.36"
)

mainClass := Some("Examples")
