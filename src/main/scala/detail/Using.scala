

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Using[A](_1: Seq[A], _2: () => java.io.Closeable) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val c = _2()

        _1.onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit { q =>
            c.close()
            f.exit(q)
        } start()
    }
}
