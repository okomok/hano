

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object IsBuggy {
    def apply(t: Throwable): Boolean = {
        t match {
            case _: java.lang.Error => true
            case _: IllegalArgumentException => true
            case _: UnsupportedOperationException => true
            case _: java.util.ConcurrentModificationException => true
            case _ => false
        }
    }
}
