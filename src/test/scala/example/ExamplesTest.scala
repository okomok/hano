

// Copyright Shunsuke Sogame 2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package examples


import com.github.okomok.hano


class ExamplesTest extends org.scalatest.junit.JUnit3Suite {

    def testContext {
        val xs = hano.async

        var i = 0
        xs onEach { x =>
            expect(())(x)
            i += 1
        } start()

        Thread.sleep(1000)
        expect(1)(i)
    }

    def testContext2 {
        val xs = hano.async

        var i = 0
        for (x <- xs) {
            expect(())(x)
            i += 1
        }

        Thread.sleep(1000)
        expect(1)(i)
    }

    def testAwait {
        val xs = hano.async

        var i = 0
        xs onEach { x =>
            expect(())(x)
            i += 1
        } await()

        expect(1)(i)
    }

    def testLoop {
        val xs = hano.async.loop

        var i = 0
        xs take {
            5
        } onEach { x =>
            expect(())(x)
            i += 1
        } await()

        expect(5)(i)
    }

    def testGenerate {
        val xs = hano.async.loop.generate(0 until 10)

        var i = 0
        xs onEach { x =>
            expect(i)(x)
            i += 1
        } await()

        expect(10)(i)
    }

    def testOnEnd {
        val xs = hano.async.loop.generate(0 until 10)

        var i = 0
        xs onEach { x =>
            i += 1
        } onEnd {
            i /= 5
        } await()

        expect(2)(i)
    }

    def testOnFailed {
        object MyError extends RuntimeException

        val xs = hano.async.loop.generate(0 until 10) onEach { x =>
            if (x == 8) {
                throw MyError
            }
        }

        var i = 0
        xs onEach { x =>
            expect(i)(x)
            i += 1
        } onFailed { e =>
            expect(MyError)(e)
            i /= 4
        } await()

        expect(2)(i)
    }

    def testShift {
        val xs = hano.async.loop.generate(0 until 10)

        var i = 0
        xs shift {
            hano.Self
        } onEach { x =>
            expect(i)(x)
            i += 1
        } start()

        expect(10)(i)
    }

    def testTimer {
        // 100ms interval
        def naturals: hano.Seq[Int] = {
            val t = new hano.Timer(true)
            val xs: hano.Seq[Unit] = t.schedule(0, 100)
            xs.generate(Stream.iterate(0)(_ + 1))
        }

        var i = 0
        naturals take {
            10
        } onEach { x =>
            expect(i)(x)
            i += 1
        } await()

        expect(10)(i)
    }

    def testListen {
        import java.awt.event.MouseEvent
        import javax.swing.event.MouseInputAdapter

        def clicks(source: java.awt.Component): hano.Seq[MouseEvent] = {
            hano.listen[MouseEvent] { * =>
                val l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = *(e)
                }
                *.addBy{source.addMouseListener(l)}
                *.removeBy{source.removeMouseListener(l)}
            }
        }

        // ...
    }
}
