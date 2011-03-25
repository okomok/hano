

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Polleach {
    def apply[A](q: java.util.Queue[A])(f: A => Unit) {
        var x = q.poll
        while (x != null) {
            f(x)
            x = q.poll
        }
    }
}
