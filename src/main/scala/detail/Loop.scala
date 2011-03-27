

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop {
    @volatile var status = Exit.Success.asStatus
    @volatile var breaks = false
    val begin = new Util.DoOnce
    val exit = new Loop.ExitImpl(this)

    def breakable(f: Reaction[_]) {
        if (breaks) {
            f.exit(status)
        }
    }
}

private[hano]
object Loop {
    private class ExitImpl(_1: Loop) extends Exit {
        override def apply(q: Exit.Status = Exit.Success) {
            // This order matters.
            _1.status = Exit.Failure(Exit.Interrupted(q))
            _1.breaks = true
        }
    }
}
