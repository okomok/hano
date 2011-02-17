
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
        val xs = hano.async.loop.generate(1 until 6)

        var out: List[Int] = Nil
        xs onEach { x =>
            out :+= x
        } await()

        expect(List(1,2,3,4,5))(out)
    }

    /**
     * You can apply `onEach` many timers.
     */
    def testOnEach2 {
        val xs = hano.async.loop.generate(1 until 4)

        var out: List[Int] = Nil
        xs onEach { x =>
            out :+= x
        } onEach { x =>
            out :+= x * 10
        } await()

        expect(List(1,10,2,20,3,30))(out)
    }
}
