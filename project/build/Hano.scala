

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


import sbt._


class Hano(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {
    val continuations = compilerPlugin("org.scala-lang.plugins" % "continuations" % buildScalaVersion)

    val scalaToolsSnapshotRepo = "scala-tools.org-SNAPSHOT" at "http://scala-tools.org/repo-snapshots/"

    val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.4.1" % "test"
    val junit = "junit" % "junit" % "4.4" % "test"
    val testng = "org.testng" % "testng" % "5.14" % "test"
    val fest = "org.easytesting" % "fest-swing" % "1.2" % "test"
    val festng = "org.easytesting" % "fest-swing-testng" % "1.2" % "test"
    val festRelease = "fest release" at "http://repository.codehaus.org"
/*
    val netty = "org.jboss.netty" % "netty" % "3.2.2.Final" % "compile"
    val nettyReleaseRepo = "repository.jboss.org" at "http://repository.jboss.org/nexus/content/groups/public/"
*/

    override def compileOptions = super.compileOptions ++
        Seq(Deprecation, Unchecked/*, ExplainTypes*/) ++ compileOptions("-P:continuations:enable")

    override def managedStyle = ManagedStyle.Maven
    override def pomExtra =
        <distributionManagement>
            <repository>
                <id>repo</id>
                <url>http://okomok.github.com/maven-repo/releases</url>
            </repository>
            <repository>
                <id>snapshot-repo</id>
                <url>http://okomok.github.com/maven-repo/snapshots</url>
            </repository>
        </distributionManagement>
    lazy val publishTo = Resolver.file("Publish", new java.io.File("../maven-repo/snapshots/"))
}