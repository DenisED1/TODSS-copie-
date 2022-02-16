name := "toddsplay"

version := "1.0"

lazy val `toddsplay` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc ,  cache , javaWs, javaJpa, filters,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "org.glassfish" % "javax.json" % "1.0.4",
  "mysql" % "mysql-connector-java" % "8.0.11"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

// Hibernate
PlayKeys.externalizeResources := false