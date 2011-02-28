
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * You can react on the exit of sequence using `onExit` family.
 */
class OnExitTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * There are three types of exit message.
     */
    def testThree {
        val xs: hano.Seq[Int] = hano.async.loop.pull(0 until 5)

        locally {
            var out: List[Int] = Nil

            xs onEach { x =>
                out :+= x
            } onExit {
                case hano.Exit.Success => out :+= 99
                case hano.Exit.Failure(t) => println("something bad happens:" + t)
            } await()

            expect(List(0,1,2,3,4,99))(out)
        }

        // The above expression is equivalent to...
        locally {
            var out: List[Int] = Nil

            xs onEach { x =>
                out :+= x
            } onEnd {
                out :+= 99
            } onFailed { t =>
                println("something bad happens: " + t)
            } await()

            expect(List(0,1,2,3,4,99))(out)
        }
    }
}
