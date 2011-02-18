
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * Using `fork`, you can apply two algorithms
 * to a sequence which is traversable only once.
 */
class ForkTest extends org.scalatest.junit.JUnit3Suite {

    def testMinMax {
        import java.lang.Math
        val it: Iterator[Int] = Iterator(5,1,6,0,3,2,4) // is traversable only once.
        val xs: hano.Seq[Int] = hano.async.loop.pull(it)

        val v1, v2 = new hano.Val[Int]
        xs fork { xs =>
            // Recall `Seq` algorithms return a single-element sequence, and
            // `Val` can be assigned with a single-element sequence.
            v1 := xs.reduceLeft(Math.min(_, _))
        } fork { xs =>
            v2 := xs.reduceLeft(Math.max(_, _))
        } start()

        expect(0)(v1())
        expect(6)(v2())
    }
}
