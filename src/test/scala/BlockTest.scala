

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano




class BlockTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = new java.util.ArrayList[Int]
        hano.block { * =>
            for (x <- *(hano.Seq(1,2,3))) {
                a.add(x); ()
            }
            a.add(99); ()
        }
        expect(hano.util.Vector(1,2,3,99))(hano.util.Vector.from(a))
    }

    def testValueDiscarding {
        val a = new java.util.ArrayList[Int]
        hano.block { * =>
            for (x <- *(hano.Seq(1,2,3))) {
                a.add(x)
                "discard me"
            }
            a.add(99)
            "discard me"
        }
        expect(hano.util.Vector(1,2,3,99))(hano.util.Vector.from(a))
    }

    def testNested {
        val a = new java.util.ArrayList[Int]
        hano.block { * =>
            for (x <- *(hano.Seq(1,2,3))) {
                a.add(x)
                for (y <- *(hano.Seq(10+x,20+x))) {
                    a.add(y); ()
                }
                a.add(98); ()
            }
            a.add(99); ()
        }
        expect(hano.util.Vector(1,11,21,98,2,12,22,98,3,13,23,98,99))(hano.util.Vector.from(a))
    }

    def testNestedValueDiscarding {
        val a = new java.util.ArrayList[Int]
        hano.block { * =>
            for (x <- *(hano.Seq(1,2,3))) {
                a.add(x)
                for (y <- *(hano.Seq(10+x,20+x))) {
                    a.add(y)
                    "discard me"
                }
                a.add(98)
                "discard me"
            }
            a.add(99)
            "discard me"
        }
        expect(hano.util.Vector(1,11,21,98,2,12,22,98,3,13,23,98,99))(hano.util.Vector.from(a))
    }

    def testRequire {
        val a = new java.util.ArrayList[(Int, Int)]
        hano.block { * =>
            val x = *.each(hano.Seq(1,2,3))
            val y = *.each(hano.Seq(2,3,4))
            *.require(x + y == 5)
            a.add((x, y))
        }
        expect(hano.util.Vector((1,4),(2,3),(3,2)))(hano.util.Vector.from(a))
    }

    def testRequire2 {
        val a = new java.util.ArrayList[(Int, Int)]
        hano.block { * =>
            val x = *.each(hano.Seq(1,2,3))
            val y = *.each(hano.Seq(2,3,4))
            *.require(x + y == 5)
            *.require(x == 2)
            a.add((x, y))
        }
        expect(hano.util.Vector((2,3)))(hano.util.Vector.from(a))
    }

}
