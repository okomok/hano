

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


//private[hano]
object ExitOnce {
    def apply(k: Exit => Unit) = new ExitOnce(k)
}

@Annotation.notThreadSafe
private[hano]
class ExitOnce(k: Exit => Unit)
{
    private[this] var exited = false

    def apply(q: Exit) {
        if (!exited) {
            exited = true
            k(q)
        }
    }

    def beforeExit(body: => Unit) {
        if (!exited) {
            body
        }
    }

    def isExited: Boolean = exited
}
