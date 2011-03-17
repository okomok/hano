

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop {
    @volatile var status = Exit.Success.asStatus
    @volatile var isActive = true
    val begin = new Util.DoOnce
    val exit = new Loop.ExitImpl(this)
}

private[hano]
object Loop {
    private class ExitImpl(_1: Loop) extends Exit {
        override def apply(q: Exit.Status = Exit.Success) {
            _1.status = Exit.Failure(Exit.ByOther(q))
            _1.isActive = false
        }
    }
}
