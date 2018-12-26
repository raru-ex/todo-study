name := """todo-study"""
organization := "net.syrup16g"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
val circeVersion = "0.10.1"

scalaVersion := "2.12.7"

libraryDependencies += guice
libraryDependencies ++= Seq(
  jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "mysql" % "mysql-connector-java" % "8.0.13"
)

// Add SBT Command
lazy val slickCodeGen = taskKey[Unit]("Execute Slick CodeGen")
slickCodeGen := (runMain in Compile).toTask(" net.syrup16g.todo.db.codegen.SlickCodeGenerator").value


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "net.syrup16g.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "net.syrup16g.binders._"
