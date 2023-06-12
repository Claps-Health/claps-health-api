
name := """claps-health-api"""
organization := ""
version := "1.0-SNAPSHOT"

scalaVersion := "2.12.9"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
//lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, LombokPlugin)
//.settings(
//  // other project settings...
//  libraryDependencies ++= Seq(
//    "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
//    "org.scala-lang" % "scala-library" % scalaVersion.value % "provided",
//// add the required annotation processing dependencies
////      "com.google.auto.service" % "auto-service" % "1.0.1",
////      "com.google.auto.factory" % "auto-factory" % "1.0.1",
//    "org.projectlombok" % "lombok" % "1.18.24" % "provided"
//  ),
//  // enable annotation processing and set the necessary options
//  javacOptions ++= Seq(
//    "-Xlint:unchecked",
//    "-source", "1.8",
//    "-target", "1.8",
//    "-encoding", "UTF-8",
//    "-proc:only",
////      "-processor", "lombok.launch.AnnotationProcessorHider$AnnotationProcessor,com.google.auto.service.AutoServiceProcessor,com.google.auto.factory.processor.AutoFactoryProcessor",
//    "-Acom.google.auto.service.ignore=true"
//  ),
//  scalacOptions ++= Seq(
//    "-unchecked",
//    "-deprecation",
//    "-feature",
//    "-language:postfixOps",
//    "-language:implicitConversions",
//    "-target:jvm-1.8",
//    "-encoding", "UTF-8",
//    "-Ywarn-dead-code",
//    "-Ywarn-numeric-widen"
//  )
//)


libraryDependencies ++= Seq(
  javaJdbc,
  javaWs,
  evolutions,
  filters
)

libraryDependencies += guice
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.dwickern" %% "swagger-play2.7" % "3.1.0"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.10"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.10"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.10"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.24" % "provided"
//crossPaths := false
//autoScalaLibrary := false
// set the source directory to include the Lombok-generated files
//sourceDirectories in Compile += baseDirectory.value / "target" / "generated-sources" / "annotations"

libraryDependencies += "com.auth0" % "java-jwt" % "4.2.1"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"
libraryDependencies += "org.bouncycastle" % "bcprov-jdk15on" % "1.54"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.25"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.3"
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4.6"
libraryDependencies += "commons-io" % "commons-io" % "2.5"
libraryDependencies += "org.apache.commons" % "commons-collections4" % "4.4"
libraryDependencies += "cafe.cryptography" % "curve25519-elisabeth" % "0.1.0"
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.9.0" % Test
libraryDependencies += "com.github.multiformats" % "java-multibase" % "v1.1.1"
libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.12"

//libraryDependencies += "net.cloudburo" % "polkadot-java" % "0.1"

val web3jVersion = "5.0.0"
libraryDependencies ++= Seq(
  "org.web3j"              %  "core"                  % web3jVersion withSources(),
  "org.web3j"              %  "crypto"                % web3jVersion withSources(), // For transaction signing and key/wallet management
//  "org.web3j"              %  "rlp"                   % web3jVersion withSources(), // Recursive Length Prefix (RLP) encoders
  "org.web3j"              %  "utils"                 % web3jVersion withSources() // Minimal set of utility classes
)

javaOptions in Test ++= Seq("-Dlogger.resource=logback-test.xml")

playEbeanModels in Compile := Seq("models.jpa.*")
playEbeanDebugLevel := 4

//evictionWarningOptions in update := EvictionWarningOptions.default
//  .withWarnTransitiveEvictions(false)
//  .withWarnDirectEvictions(false)
