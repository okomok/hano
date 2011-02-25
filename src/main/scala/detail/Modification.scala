

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
final class Modification(msg: => String) {
    @volatile private[this] var _ing = false

    def apply[A](body: => A): A = {
        if (_ing) {
            throw new java.util.ConcurrentModificationException(msg) with SeriousException
        }
        try {
            _ing = true
            body
        } finally {
            _ing = false
        }
    }
}
