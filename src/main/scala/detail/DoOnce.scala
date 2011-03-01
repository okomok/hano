

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// Note this is not thread-safe.
// @volatile is nothing but a hint.

private[hano]
class DoOnce {
    @volatile private[this] var _done = false

    def isDone: Boolean = _done

    def apply(body: => Unit) {
        if (!_done) {
            _done = true
            body
        }
    }
}
