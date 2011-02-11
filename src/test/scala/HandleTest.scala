

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class HandleTest extends org.scalatest.junit.JUnit3Suite {

    def testEach {
        val xs = hano.async.loop.generate(0 until 5)
        val out = new java.util.ArrayList[Int]

        xs handleEach { x =>
            x == 3
        } onEach {
            out.add(_)
        } await()

        expect(hano.Iter(0,1,2,4))(hano.Iter.from(out))
    }

    def testExit {
        val xs = hano.Self.loop.generate(0 until 10)
        val out = new java.util.ArrayList[Int]

        object MyError1 extends RuntimeException

        var t: Throwable = null
        intercept[MyError1.type] {
            xs handleExit {
                case hano.Exit.Failed(MyError1) => ()
            } onFailed { s =>
                t = s
            } onEach { x =>
                if (x == 3) {
                    throw MyError1
                }
            } start()
        }

        expect(null)(t)
    }

    def testExitNotHandled {
        val xs = hano.Self.loop.generate(0 until 10)
        val out = new java.util.ArrayList[Int]

        object MyError1 extends RuntimeException
        object MyError2 extends RuntimeException

        var t: Throwable = null
        intercept[MyError1.type] {
            xs handleExit {
                case hano.Exit.Failed(MyError2) => ()
            } onFailed { s =>
                t = s
            } onEach { x =>
                if (x == 3) {
                    throw MyError1
                }
            } start()
        }

        expect(MyError1)(t)
    }
}
