

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop {
    @volatile var status = Exit.Success.asStatus
    @volatile var isActive = true

    val begin = new DoOnce

    def reset() {
        status = Exit.Success
        isActive = true
    }

    def end(q: Exit.Status) {
        status = Exit.Failure(Exit.ByOther(q))
        isActive = false
    }
}
