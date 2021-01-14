import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val akkaVersion = "2.5.23"

  val compile = Seq(
    "uk.gov.hmrc"                             %% "simple-reactivemongo"      % "7.30.0-play-26",
    "uk.gov.hmrc"                             %% "bootstrap-backend-play-26" % "2.24.0",
    "uk.gov.hmrc"                             %% "work-item-repo"            % "7.6.0-play-26",
    "org.typelevel"                           %% "cats-core"                 % "2.1.0",
    "org.julienrf"                            %% "play-json-derived-codecs"  % "7.0.0",
    "com.github.kxbmap"                       %% "configs"                   % "0.4.4",
    "com.googlecode.owasp-java-html-sanitizer" % "owasp-java-html-sanitizer" % "20191001.1",
    "com.github.ghik"                          % "silencer-lib"              % "1.6.0" % Provided cross CrossVersion.full
  )

  val test = Seq(
    "org.scalatest"              %% "scalatest"                  % "3.0.8"          % "test",
    "com.typesafe.play"          %% "play-test"                  % current          % "test",
    "org.scalamock"              %% "scalamock"                  % "4.2.0"          % "test",
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14"  % "1.2.1"          % "test",
    "org.pegdown"                 % "pegdown"                    % "1.6.0"          % "test, it",
    "uk.gov.hmrc"                %% "service-integration-test"   % "0.10.0-play-26" % "test, it",
    "org.scalatestplus.play"     %% "scalatestplus-play"         % "3.1.2"          % "test, it",
    "uk.gov.hmrc"                %% "reactivemongo-test"         % "4.21.0-play-26" % "test",
    "com.eclipsesource"          %% "play-json-schema-validator" % "0.9.5"          % "test",
    "com.typesafe.akka"          %% "akka-testkit"               % akkaVersion      % Test
  )


//  val compile = Seq(
//    "uk.gov.hmrc"             %% "bootstrap-backend-play-27"  % "3.3.0",
//    "uk.gov.hmrc"             %% "simple-reactivemongo"       % "7.31.0-play-27"
//  )
//
//  val test = Seq(
//    //original
////    "uk.gov.hmrc"             %% "bootstrap-test-play-27"   % "3.3.0" % Test,
////    "org.scalatest"           %% "scalatest"                % "3.2.3"  % Test,
////    "com.typesafe.play"       %% "play-test"                % current  % Test,
////    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.36.8" % "test, it",
////    "org.scalatestplus.play"  %% "scalatestplus-play"       % "4.0.3"  % "test, it"
//
//  )
}
