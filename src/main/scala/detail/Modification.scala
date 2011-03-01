

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


@annotation.visibleForTesting
final class Modification(msg: => String) {
    @volatile private[this] var _ing: Thread = null

    def apply[A](body: => A): A = {
        val cur = Thread.currentThread()

        if ((_ing ne null) && (_ing ne cur)) {
            throw new java.util.ConcurrentModificationException(msg) with SeriousException
        }

        try {
            _ing = cur
            body
        } finally {
            _ing = null
        }
    }
}
