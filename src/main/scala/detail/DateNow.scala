

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// For `Timer` to be a conforming `Process`.

// You sometimes have to prefer aDate to aDelay, because
// `{ aJTimer.schedule(t1, aDelay); aJTimer.schedule(t2, aDelay+1) }`
// possibly evaluates `t2` then `t1`, for some reason.


import java.util.Date


private[hano]
class DateNow {
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
