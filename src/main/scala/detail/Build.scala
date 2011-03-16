

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


object Build {
    // Note you can't share a builder, e.g. mutable.ArrayBuffer's.
    def apply[A, To](from: Iter[A], b: Builder[A, To]): To = {
        for (x <- from.able) {
            b += x
        }
        b.result
    }
}
