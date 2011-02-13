

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class FoldTest extends org.scalatest.junit.JUnit3Suite {

    def testMinMaxCold {
        import java.lang.Math

        var v1, v2: Option[Int] = None

        hano.Seq.from(Iterator(5,1,6,0,3,2,4)) fork { xs =>
            xs reduceLeft { (a, x) =>
                Math.min(a, x)
            } onEach { v =>
                v1 = Some(v)
            }
        } fork { xs =>
            xs reduceLeft { (a, x) =>
               Math.max(a, x)
            } onEach { v =>
                v2 = Some(v)
            }
        } start()

        expect(0)(v1.get)
        expect(6)(v2.get)
    }

    def testMinMaxHot {
        import java.lang.Math

        val xs = hano.async.loop.generate(Seq(5,1,6,0,3,2,4)).once

        val v1, v2 = new hano.Val[Int]

        xs fork { xs =>
            xs reduceLeft { (a, x) =>
                Math.min(a, x)
            } onEach { x =>
                v1() = x
            }
        } fork { xs =>
            xs reduceLeft { (a, x) =>
               Math.max(a, x)
            } onEach { x =>
                v2() = x
            }
        } start()

        expect(0)(v1())
        expect(6)(v2())
    }

    def testNoSuchElement {
        val xs = hano.async.loop.generate(Seq.empty[Int])
        var s: Throwable = null
        xs reduceLeft { (a, x) => a + x } onFailed { t => s = t } await()
        assert(s.isInstanceOf[NoSuchElementException])
    }

    def testFoldLeft {
        val xs = hano.async.loop.generate(Seq(5,1,6,0,3,2,4))

        val that = new hano.Val[String]
        ("9" /: xs) { (a, x) => a + x.toString } onEach { v => that() = v } start()

        expect("95160324")(that())
    }

    def testFoldLeftEmpty {
        val xs = hano.async.loop.generate(Seq())

        val that = new hano.Val[String]
        ("9" /: xs) { (a, x) => a + x.toString } onEach { v => that() = v } start()

        expect("9")(that())
    }

    /*
    def testReduceLeftEmpty {
        val xs = hano.async.loop.generate(Seq())

        val that = new hano.Val[String]
        xs reduceLeft { (a: String, x) => a + x.toString } onEach { v => that() = v } start()

        intercept[NoSuchElementException] {
            that()
        }
    }
    */
}
