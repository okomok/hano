

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class NotRecur {
    private[this] var entered = false
    def apply(body: => Unit) {
        if (!entered) {
            entered = true
            try {
                body
            } finally {
                entered = false
            }
        }
    }
}
