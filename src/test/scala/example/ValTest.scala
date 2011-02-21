
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `Val` is an immutable single-element sequence whose value can be set later.
 * `Val` methods are thread-safe and lock-free.
 */
class ValTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `Val` is immutable, meaning that you can set only the same values.
     */
    def testSet {
        val v = new hano.Val[Int]
        v() = 7 // set

        var out: Option[Int] = None

        // `Val` is a single-element sequence.
        v onEach { x =>
            out = Some(x)
        } await()

        expect(7)(out.get)

        v() = 7 // the same value is OK. (no effects)

        intercept[hano.Val.MultipleAssignmentException] {
            v() = 8 // different value will throw.
        }
    }

    /**
     * Reactions on `Val` run in asynchronous context.
     */
    def testAsync {
        val v = new hano.Val[Int]

        var out: Option[Int] = None
        v onEach { x =>
            out = Some(x)
        } start()

        v() = 7
        Thread.sleep(1000) // You might have to wait for the asynchronous job of `Val`.

        expect(7)(out.get)
    }

    /**
     * The assigned value can be retrieved using `apply()`
     */
     def testGet {
         val v = new hano.Val[Int]

         new Thread {
             override def run() {
                 Thread.sleep(1000)
                 // `Val` methods are thread-safe.
                 v() = 7
             }
         } start()

         expect(7)(v()) // waits for the thread to have done the job.
     }

    /**
     * You can assign a single-element sequence to `Val`.
     */
     def testAssign {
         val xs = hano.async.loop.pull(0 until 9)

         // Recall a `Seq` algorithm returns a single-element `Seq`.
         val x: hano.Seq[Int] = xs find { x => x == 7 }

         locally {
             val v = new hano.Val[Int]
             v := x // assign
             expect(7)(v())
         }

         // The above expression is equivalent to...
         locally {
             val v = hano.Val(x)
             expect(7)(v())
         }
     }
}
