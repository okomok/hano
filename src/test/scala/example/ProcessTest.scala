
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `Process` is an infinite sequence of the `Unit`s.
 */
class ProcessTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `Self` is a `Process` where reactions are invoked in `start` call-site.
     */
    def testSelf {
        val us: hano.Seq[Unit] = hano.Self

        locally {
            var i = 0
            us.onEach { x =>
            // reaction block
                expect(())(x) // The element is the `Unit`.
                if (i == 5) {
                    hano.break
                }
                i += 1
            }.start

            expect(5)(i)
        }

        // By definition, the above expression is equivalent to...
        locally {
            var i = 0
            for (x <- us) {
                expect(())(x)
                if (i == 5) {
                    hano.break
                }
                i += 1
            }

            expect(5)(i)
        }
    }

    /**
     * `async` creates a `Process` where reactions are invoked in a thread-pool.
     */
    def testAsync {
        val us: hano.Seq[Unit] = hano.async

        locally {
            var i = 0
            us.onEach { x =>
            // Don't bother about multi-threaded issue.
            // Any `Seq` guarantees reactions are invoked in sequential fashion.
                expect(())(x)
                if (i == 5) {
                    hano.break
                }
                i += 1
            }.start

            Thread.sleep(500) // wait for the thread-pool has done the reaction.
            expect(5)(i)
        }

        // Even better, `await` instead of `start` waits until a sequence ends.
        locally {
            var i = 0
            us.onEach { x =>
                expect(())(x)
                if (i == 5) {
                    hano.break
                }
                i += 1
            }.await

            expect(5)(i)
        }
    }
}
