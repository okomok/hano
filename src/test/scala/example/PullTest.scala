
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `pull` retrieves elements from an `Iterable`.
 */
class PullTest extends org.scalatest.junit.JUnit3Suite {

    def testIterable {
        // Recall `async` is an infinite sequence of `Unit`s.
        val us: hano.Seq[Unit] = hano.async

        // `Unit`s are replaced with the `Iterable`,
        // and then turned into a finite sequence.
        val xs: hano.Seq[Int] = us.pull(0 until 4)

        var out: List[Int] = Nil
        xs.onEach { x =>
            out :+= x
        }.await

        expect(List(0,1,2,3))(out)
    }
}
