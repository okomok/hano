
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `Context` is a single-element sequence of the `Unit`.
 */
class ContextTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `Self` is a `Context` where reactions are invoked in `start` call-site.
     */
    def testSelf {
        val us: hano.Seq[Unit] = hano.Self

        locally {
            var i = 0
            us onEach { x =>
            // reaction block
                expect(())(x) // The element is the `Unit`.
                i += 1
            } start()

            expect(1)(i) // `Context` has only one element.
        }

        // By definition, the above expression is equivalent to...
        locally {
            var i = 0
            for (x <- us) {
                expect(())(x)
                i += 1
            }

            expect(1)(i)
        }
    }

    /**
     * `async` creates a `Context` where reactions are invoked in a thread-pool.
     */
    def testAsync {
        val us: hano.Seq[Unit] = hano.async

        locally {
            var i = 0
            us onEach { x =>
            // Don't bother about multi-threaded issue.
            // Any `Seq` guarantees reactions are invoked in serialized fashion.
                expect(())(x)
                i += 1
            } start()

            Thread.sleep(500) // wait for the thread-pool has done the reaction.
            expect(1)(i)
        }

        // Even better, `await` instead of `start` waits until a sequence ends.
        locally {
            var i = 0
            us onEach { x =>
                expect(())(x)
                i += 1
            } await()

            expect(1)(i)
        }
    }
}
