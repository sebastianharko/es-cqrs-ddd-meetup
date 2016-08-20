name := "simpleproject"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "3.4.0" withSources(),
  "com.typesafe.akka" %% "akka-actor" % "2.4.9-RC2" withSources(),
  "com.typesafe.akka" %% "akka-cluster" % "2.4.9-RC2" withSources(),
  "com.typesafe.akka" %% "akka-persistence" % "2.4.9-RC2" withSources(),
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.9-RC2" withSources(),
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.6-RC1" withSources(),
  "de.heikoseeberger" %% "akka-http-json4s" % "1.8.0" withSources(),
  "org.json4s" %% "json4s-jackson" % "3.2.9" withSources(),
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.9-RC2" withSources(),
  "ch.qos.logback" % "logback-classic" % "1.1.3" withSources()
)

