

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest; package doctest


    import com.github.okomok.hano
        import junit.framework.Assert._

    class VarTest extends org.scalatest.junit.JUnit3Suite {
        def testTrivial {
            // `Var` is a mutable one-element sequence.
            val a = new hano.Var(1)
            val b = new hano.Var(2)

            var z = 0
            for (x <- a; y <- b) {
                z = x + y
            }

            assertEquals(3, z)
            a := 7
            assertEquals(9, z)
            b := 35
            assertEquals(42, z)
        }
    }

