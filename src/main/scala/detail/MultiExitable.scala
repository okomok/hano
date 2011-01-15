

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// Accepts multiple exit calls, though it's illegal.

private[hano]
class MultiExitable[A](f: Reaction[A]) extends Reaction[A] with java.io.Flushable {
    private[this] var exited = false
    override def flush() {
        if (f.isInstanceOf[java.io.Flushable]) {
            f.asInstanceOf[java.io.Flushable].flush()
        }
    }
    override def apply(x: A) = f(x)
    override def exit(q: Exit) {
        if (!exited) {
            exited = true
            f.exit(q)
        } else {
            q match {
                case Exit.Failed(t) => LogErr(t, "abandoned exception in Generator")
                case _ => ()
            }
        }
    }
}
