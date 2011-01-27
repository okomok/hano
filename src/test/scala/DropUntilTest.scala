

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import hano.Reactor
import junit.framework.Assert._
import scala.actors.Actor


class DropUntilTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val ctx = hano.Context.act
        val xs = new hano.Channel[Int]//(ctx)
        val ys = new hano.Set[Int](10)//, ctx)

        val zs = ys dropUntil xs
        ys member 1
        ys member 2
        ys member 3
        xs write 999
        //xs write 999
        ys member 4
        ys member 5
        ys member 6

 //       zs.toIter.toString
for (y <- zs) {
            println(y)
        }

        Thread.sleep(2000)
//        println(ys.toIter)

        expect(hano.Iter(1,2,3,4,5,6))(hano.Iter.from(hano.AsyncGenerator.iterable(zs)))
    }

    def testTrivial2: Unit = {
        val out = new java.util.ArrayList[Int]

        val b = Reactor.singleThreaded()
        val a = Reactor.singleThreaded { r =>
            r collect {
                case e: Int => e
            } dropUntil {
                b
            } onEach { x =>
                out.add(x)
            } start
        }

        a ! 1
        a ! 2
        a ! 3
        b ! "go"
        a ! 4
        a ! 5
        assertEquals(hano.Iter(4,5), hano.Iter.from(out))
    }

}
