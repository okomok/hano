

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class NoEnd[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        _1 onEach {
            f(_)
        } onExit {
            case Exit.End => ()
            case q => f.exit(q)
        } start()
    }
}
