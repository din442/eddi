organization := "com.jigsawstudio"
name := "eddi"
version := "0.1"
scalaVersion := "2.12.6" 
sbtVersion := "1.3.5"

resolvers := Seq("Concurrent Maven Repo" at "http://conjars.org/repo")
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.4.0",
  "com.h2database"  %  "h2"                % "1.4.200",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.1",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.10.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.1",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
  "org.postgresql"  %  "postgresql"        % "42.2.9",
  "org.scalatest"   %% "scalatest"         % "3.1.0"    % "test",
)


