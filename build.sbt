val flywayVersion     = "8.5.12" // manages database migrations
val postgresVersion   = "42.3.6" // Java database connectivity (JDBC) driver for PostgreSQL
val slf4jVersion      = "1.7.36" // logging framework
val logbackVersion    = "1.4.5"  //logging framework implementation
val jansiVersion      = "1.18"   // Colored log lines
val zioJsonVersion    = "0.4.2"  // JSON serialization library for ZIO
val zioLoggingVersion = "2.1.8"  // logging library for ZIO
val zioQuillVersion   = "4.6.0"  // compile-time database query library for ZIO
val zioVersion        = "2.0.7"  // Scala library for asynchronous and concurrent programming
val zioConfigVersion  = "3.0.7"  // Configuration library for ZIO
val hikariVersion     = "5.0.1"  // A JDBC connection pool
val zioTapirVersion   = "1.2.8"  // Server, endpoint definition and documentation for ZIO
val ironVersion       = "2.0.0"  //Refined types

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

ThisBuild / fork := true

lazy val root = (project in file("."))
  .settings(
    name             := "zio-subscription",
    idePackagePrefix := Some("com.lamoroso.example"),
    scalacOptions ++= Seq(
      "-Wunused:all"
    ),
    libraryDependencies ++= Seq(
      "dev.zio"                     %% "zio"                     % zioVersion,
      "dev.zio"                     %% "zio-json"                % zioJsonVersion,
      "dev.zio"                     %% "zio-config"              % zioConfigVersion,
      "dev.zio"                     %% "zio-config-typesafe"     % zioConfigVersion,
      "dev.zio"                     %% "zio-config-magnolia"     % zioConfigVersion,
      "io.getquill"                 %% "quill-jdbc-zio"          % zioQuillVersion,
      "org.postgresql"               % "postgresql"              % postgresVersion,
      "com.zaxxer"                   % "HikariCP"                % hikariVersion,
      "org.flywaydb"                 % "flyway-core"             % flywayVersion,
      "dev.zio"                     %% "zio-logging-slf4j"       % zioLoggingVersion,
      "dev.zio"                     %% "zio-logging"             % zioLoggingVersion,
      "org.slf4j"                    % "slf4j-api"               % slf4jVersion,
      "ch.qos.logback"               % "logback-classic"         % logbackVersion,
      "org.fusesource.jansi"         % "jansi"                   % jansiVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server"   % zioTapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-zio"          % zioTapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % zioTapirVersion,
      "io.github.iltotore"          %% "iron"                    % ironVersion,
      "io.github.iltotore"          %% "iron-zio"                % ironVersion,
      "io.github.iltotore"          %% "iron-zio-json"           % ironVersion
    )
  )
