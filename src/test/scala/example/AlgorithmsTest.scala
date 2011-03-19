
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * Unlike `scala.collections`, algorithms on `Seq` return a single-element `Seq`.
 */
class AlgorithmsTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `head` returns a single-element `Seq` whose element is,
     * of course, the head element of sequence.
     */
    def testHead {
        val xs: hano.Seq[Int] = hano.async.pull(5 until 8)

        var out: Option[Int] = None
        xs.head onEach { x =>
            out = Some(x)
        } await()

        expect(5)(out.get)
    }

    /**
     * Ditto `find`.
     */
    def testFind {
        val xs = hano.async.pull(5 until 8)

        locally {
            var out: Option[Int] = None
            xs find { x =>
                x % 2 == 0
            } onEach { x =>
                out = Some(x)
            } await()

            expect(6)(out.get)
        }

        // What happens if not found?
        locally {
            var out: Option[Int] = None
            intercept[NoSuchElementException] {
                xs find { x =>
                    x == 10
                } onEach { x =>
                    out = Some(x)
                } await()
            }

            expect(None)(out)
        }
    }
}
