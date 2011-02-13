

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import org.testng.annotations._


class FoldTest extends org.scalatest.junit.JUnit3Suite {
/*
    def testFork {
        import java.lang.Math.{min, max}

        val xs = hano.async.loop.generate(Seq(5,1,6,0,3,2,4).once

        val v1, v2 = new Val[Int]

        xs fork { xs =>
            xs reduceLeft { (a, x) =>
                min(a, x)
            } onEach { x =>
                v1() = x
            }
        } fork { xs =>
            xs reduceLeft { (a, x) =>
               max(a, x)
            } onEach { x =>
                v2() = x
            }
        } start()

        expect(0)(v1())
        expect(6)(v2())
    }
    */
}
