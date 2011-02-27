

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
final class Modification(msg: => String) {
    @volatile private[this] var _ing: Thread = null

    def apply[A](body: => A): A = {
        if (_ing eq null) {
            _ing = Thread.currentThread()
        }

        if (_ing ne Thread.currentThread()) {
            throw new java.util.ConcurrentModificationException(msg) with SeriousException
        }
        try {
            _ing = Thread.currentThread()
            body
        } finally {
            _ing = null
        }
    }
}
