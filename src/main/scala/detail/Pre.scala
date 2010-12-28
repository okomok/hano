

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Pre {

    def positive(n: Int, msg: String) {
        if (n <= 0) {
            throw new IllegalArgumentException(msg + ": " + n + " shall be positive.")
        }
    }

    def nonnegative(n: Int, msg: String) {
        if (n < 0) {
            throw new IllegalArgumentException(msg + ": " + n + " shall be nonnegative.")
        }
    }

    def range(n: Int, m: Int, msg: String) {
        if (n > m) {
            throw new IllegalArgumentException(msg + ": " + Tuple2(n, m) + " shall be a valid range.")
        }
    }

    def notEmpty[A](xs: Iter[A], msg: String) {
        if (xs.able.isEmpty) {
            throw new UnsupportedOperationException(msg + ": sequence shall not be empty.")
        }
    }

    def sameSize[A, B](xs: scala.collection.IndexedSeq[A], ys: scala.collection.IndexedSeq[B], msg: String) {
        val (x, y) = (xs.size, ys.size)
        if (x != y) {
            throw new UnsupportedOperationException(msg + ": sequences shall be the same size, but " + (x, y))
        }
    }

}
