

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


import com.github.okomok.hano

import hano.Arm
import junit.framework.Assert._


class ForTest extends org.scalatest.junit.JUnit3Suite {

    class MyFile(val name: String) extends java.io.Closeable {
        var disposed = false
        override def close = {
            assertFalse("disposed twice: " + name, disposed)
            disposed = true
        }
    }

    def identity[X](x: X) = x

    def testGocha: Unit = {
        val fs = new java.util.ArrayList[MyFile]
        for {
            name <- Array("foo", "bar", "buz")
            file <- Arm.from(new MyFile(name))
        } {
            fs.add(file)
        }

        assertEquals(3, fs.size)
        for (file <- hano.Iter.from(fs).able) {
            assertTrue(file.disposed)
        }
    }


    def make3(r1: Arm[Int], r2: Arm[Int], r3: Arm[Int]): hano.Seq[Int] = {
        for {
            s1 <- r1
            s2 <- r2
            s3 <- r3
        } yield {
            s1 + s2 + s3
        }
    }

    def testBig {
        val r1 = TrivialResource(1)
        val r2 = TrivialResource(2)
        val r3 = TrivialResource(3)
        val r4 = TrivialResource(4)
        val r5 = TrivialResource(5)
        val r6 = TrivialResource(6)

        for {
            x <- make3(r1, r2, r3)
            y <- make3(r4, r5, r6)
        } {
            assertEquals(1+2+3+4+5+6, x + y)
        }
    }

}
