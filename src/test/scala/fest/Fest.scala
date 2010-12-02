


// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.madatest
package sequencetest.reactivetest.festtest


abstract class FestTestNGSuite extends
    org.fest.swing.testng.testcase.FestSwingTestngTestCase with
    org.scalatest.testng.TestNGSuite


abstract class NotFestSuite extends NotSuite {
    protected def onSetUp: Unit = throw new Error("NotFestSuite.onSetUp")
    def robot = throw new Error("NotFestSuite.robot")
}


object FestUtil {

    type Component = {
        def setName(s: String): Unit
        def getText(): String
    }

    sealed class TextAsName[T <: Component](c: T) {
        def textAsName: T = {
            c.setName(c.getText)
            c
        }
    }
    implicit def textAsName[T <: Component](c: T): TextAsName[T] = new TextAsName(c)

}
