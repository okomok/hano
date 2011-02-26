

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// For conforming Timer.context
private[hano]
class ZeroDelay {
    private[this] var i = 0L
    private[this] var past = 0L

    def apply(): Long = {
        val now = java.lang.System.currentTimeMillis()
        if (past == now) {
            i += 1
        } else {
            i = 0L
        }
        past = now
        i
    }
}
