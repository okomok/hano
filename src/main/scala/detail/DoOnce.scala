

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DoOnce {
    @volatile private[this] var _done = false

    def apply(body: => Unit) {
        if (!_done) {
            _done = true
            body
        }
    }

    def isDone : Boolean = _done
}
