import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play20sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "reactivemongo" %% "reactivemongo" % "0.1-SNAPSHOT" cross CrossVersion.full,
    "play.modules.reactivemongo" %% "play2-reactivemongo" % "0.1-SNAPSHOT" cross CrossVersion.full,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
     resolvers += "sgodbillon" at "https://bitbucket.org/sgodbillon/repository/raw/master/snapshots/"
  )

}
