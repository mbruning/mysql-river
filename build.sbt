import AssemblyKeys._

name := """scala-mysql-river"""

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3-M1"

libraryDependencies += "com.typesafe" % "scalalogging-slf4j_2.10" % "1.0.1"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "0.19.8"

libraryDependencies += "junit" % "junit" % "4.11"

libraryDependencies += "org.scala-tools.testing" % "specs_2.9.3" % "1.6.9"

test in assembly := {}

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
    {
        case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
        case _ => MergeStrategy.first
    }
}