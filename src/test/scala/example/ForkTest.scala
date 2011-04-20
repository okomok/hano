
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * Using `fork`, you can apply two algorithms to a mutable sequence.
 */
class ForkTest extends org.scalatest.junit.JUnit3Suite {

    def testMotivation {
        // `Iterator` is traversable once only, that is, *mutable*.
        val it: Iterator[Int] = Iterator(5,1,6,0,3,2,4)

        val xs: hano.Seq[Int] = hano.async.pull(it)

        // The first time is ok.
        expect(0)(hano.Val(xs.min).get)

        // For the second time, `xs` is mutated to empty one.
        expect(true)(hano.Val(xs.isEmpty).get)
    }

    def testMinMax {
        val it: Iterator[Int] = Iterator(5,1,6,0,3,2,4)
        val xs: hano.Seq[Int] = hano.async.pull(it)

        val v1, v2 = new hano.Val[Int]
        xs.fork { xs =>
            // Recall `Seq` algorithms return a single-element sequence, and
            // `Val` can be assigned with a single-element sequence.
            v1 := xs.min
        } fork { xs =>
            v2 := xs.max
        } start()

        expect(0)(v1())
        expect(6)(v2())
    }
}
