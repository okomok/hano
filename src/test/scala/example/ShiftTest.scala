
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `shift` changes a process where reactions are invoked.
 */
class ShiftTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `shift` to `Self` guarantees reactions has been invoked in `start` call-site.
     */
    def testShiftToSelf {
        val xs = hano.async.pull(0 until 4)

        var out: List[Int] = Nil
        xs.shift {
            hano.Self
        }.onEach { x =>
            out :+= x
        }.start // Notice `await` is unneeded here.

        expect(List(0,1,2,3))(out)
    }

    /**
     * Swing often requires a method to be invoked in the event-dispatch-thread(EDT).
     */
    def testShiftToEdt {
        val xs = hano.async.pull(0 until 4)

        var out: List[Int] = Nil
        xs.shift {
            hano.Edt // `Edt` is a `Process`.
        }.onEach { x =>
            // This reaction is invoked in the EDT.
            // ...
        }.start
    }
}
