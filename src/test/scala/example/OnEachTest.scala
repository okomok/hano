
package com.github.okomok.hanotest.example
import com.github.okomok.hano


/**
 * `onEach` takes a reaction which is invoked by the sequence.
 */
class OnEachTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `onEach`, the most important method
     */
    def testTrivial {
        val xs = hano.async.loop.generate(0 until 6)

        var i = 0
        xs onEach { x =>
            expect(i)(x)
            i += 1
        } await()

        expect(6)(i)
    }

    /**
     * You can apply `onEach` many timers.
     */
    def testOnEachOnEach {
        val xs = hano.async.loop.generate(0 until 6)

        var i = 0
        var j = 6
        xs onEach { x =>
            expect(i)(x)
            j -= 1
        } onEach { x =>
            expect(i)(x)
            i += 1
        } await()

        expect(6)(i)
        expect(0)(j)
    }
}
