
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `Timer` can create a scheduled sequence.
 */
class TimerTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * Natural number sequence generated every one second
     */
    def naturals: hano.Seq[Int] = {
        // Prepare a `Timer` in a daemon thread.
        val t = new hano.Timer(true)

        // `schedule` method creates a breakable infinite sequence of the `Unit`s.
        val us: hano.Seq[Unit] = t.schedule(0, 1000)

        // Replace the `Unit`s with `Stream` elements.
        us.pull(Stream.iterate(0)(_ + 1))
    }

    def testNaturals {
        var out: List[Int] = Nil
        naturals.take {
            5
        } onEach { x =>
            out :+= x
        } await()

        expect(List(0,1,2,3,4))(out)
    }
}
