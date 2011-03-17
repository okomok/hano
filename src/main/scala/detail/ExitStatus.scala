

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ExitStatus(_1: Seq[_]) extends SeqAdapter.Of[Exit.Status](_1) with SingleSeq[Exit.Status] {
    override def forloop(f: Reaction[Exit.Status]) {
        _1.onEnter {
            f.enter(_)
        } onExit { q =>
            f(q)
            f.exit()
        } start()
    }
}
