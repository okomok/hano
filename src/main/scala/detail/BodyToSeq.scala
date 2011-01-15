

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class BodyToSeq[A](body: Reaction[A] with java.io.Flushable => Unit) extends Seq[A] {
    override def context = Context.self
    override def forloop(f: Reaction[A]) {
        val g = new MultiExitable(f)
        try {
            body(g)
            g.exit(Exit.End)
        } catch {
            case t: Throwable => g.exitNothrow(Exit.Failed(t))
        }
    }
}
