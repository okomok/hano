

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


object SeriousError {
    def unapply(t: Throwable): Option[Throwable] = {
        t match {
            case _: java.lang.Error => Some(t)
            case _: IllegalArgumentException => Some(t)
            case _: UnsupportedOperationException => Some(t)
            case _: java.util.ConcurrentModificationException => Some(t)
            case _ => None
        }
    }
}
