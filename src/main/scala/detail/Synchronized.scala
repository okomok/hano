

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Synchronized {
    def apply[A](l: java.util.concurrent.locks.ReentrantLock)(body: => A): A = {
        l.lock()
        try {
            body
        } finally {
            l.unlock()
        }
    }
}
