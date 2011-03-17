

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains some preconditions.
 */
object Require {

    def nonnegative(n: Long, msg: => String) {
        require(n >= 0, msg + ": " + n + " shall be nonnegative")
    }

    def positive(n: Long, msg: => String) {
        require(n > 0, msg + ": " + n + " shall be positive")
    }

    def range(n: Int, m: Int, msg: => String) {
        require(n <= m, msg + ": " + Tuple2(n, m) + " shall be a valid range")
    }

    def notSelf(p: Process, msg: => String) {
        require(p ne Self, msg + " doesn't support Self process")
    }

    def notUnknown(p: Process, msg: => String) {
        require(p ne Unknown, msg + " doesn't support Unknown process")
    }
}
