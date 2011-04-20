
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * Like `scala.collections`, `Seq` contains
 * many methods to return a view to underlying one.
 */
class ViewsTest extends org.scalatest.junit.JUnit3Suite {

    /**
     * `map` and `filter, the famous ones.
     */
    def testMapFilter {
        val xs: hano.Seq[Int] = hano.async.pull(0 until 3)

        var out: List[String] = Nil
        xs.map { x =>
            x.toString
        } filter { str =>
            str != "1"
        } onEach { str =>
            out :+= str
        } await()

        expect(List("0","2"))(out)
    }

    /**
     * `append`(or `++`) concatenates two sequences.
     */
    def testAppend {
        val xs: hano.Seq[Int] = hano.async.pull(0 until 3)
        val ys: hano.Seq[Int] = new hano.Timer().schedule(0, 100).pull(3 until 6)

        var out: List[Int] = Nil
        (xs ++ ys).onEach { x =>
            out :+= x
        } await()

        expect(List(0,1,2,3,4,5))(out)
    }
}
