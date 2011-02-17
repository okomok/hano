
package com.github.okomok.hanotest.example
import com.github.okomok.hano


/**
 * `loop` cycles a sequence until closed.
 */
class LoopTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * Recall `async` returns a single-element sequence of the Unit.
     */
    def testContext {
        val xs = hano.async.loop // infinite sequence of the `Unit`s.

        var i = 0
        xs onEach { x =>
            expect(())(x)
            i += 1
            if (i == 5) {
                xs.close()
            }
        } await()

        expect(5)(i)
    }

    /**
     * `generate` replaces the elements with those of `scala.Iterable`.
     */
    def testGenerateFromIterable {
        val xs = hano.async.loop.generate(0 until 4)

        var i = 0
        xs onEach { x =>
            expect(i)(x)
            i += 1
        } await()

        expect(4)(i)
    }

    /**
     * `loop` can loop.
     */
    def testLoopLoop {
        val xs = hano.async.loop.generate(0 until 3)

        var i = 0
        var out: List[Int] = Nil
        xs.loop take {
            8
        } onEach { x =>
            out :+= x
            i += 1
        } await()

        expect(List(0,1,2,0,1,2,0,1))(out)
    }
}
