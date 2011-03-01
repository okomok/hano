
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `loop` cycles a sequence until closed.
 */
class LoopTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * Recall `async` returns a single-element sequence of the `Unit`.
     */
    def testContext {
        // infinite sequence of the `Unit`s.
        val us: hano.Seq[Unit] = hano.async.loop

        var i = 0
        us onEach { x =>
            expect(())(x)
            i += 1
            if (i == 5) {
                hano.break()
            }
        } await()

        expect(5)(i)
    }

    /**
     * `pull` replaces the elements with those of `scala.Iterable`.
     */
    def testPullIterable {
        // `Unit`s are replaced with the `Iterable`.
        val xs: hano.Seq[Int] = hano.async.loop.pull(0 until 4)

        var out: List[Int] = Nil
        xs onEach { x =>
            out :+= x
        } await()

        expect(List(0,1,2,3))(out)
    }

    /**
     * `loop` can loop.
     */
    def testLoopLoop {
        val xs: hano.Seq[Int] = hano.async.loop.pull(0 until 3)

        var out: List[Int] = Nil
        xs.loop take {
            8
        } onEach { x =>
            out :+= x
        } await()

        expect(List(0,1,2,0,1,2,0,1))(out)
    }
}
