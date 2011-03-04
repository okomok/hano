

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// For `Timer` to be a conforming `Context`


import java.util.Date


private[hano]
class Now {
    private[this] val past = new Date(0L)

    def apply(): Date = synchronized {
        val now = new Date

        if (past.getTime >= now.getTime) {
            past.setTime(past.getTime + 1)
        } else {
            past.setTime(now.getTime)
        }

        past
    }

    def +(i: Long): Date = new Date(apply().getTime + i)
}
