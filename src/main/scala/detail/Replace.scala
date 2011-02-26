

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Replace[A](_1: Seq[A], _2: Iter[A]) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        val it = _2.ator

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (it.hasNext) {
                f(it.next)
            } else {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}

private[hano]
class ReplaceRegion[A](_1: Seq[A], _2: Int, _3: Int, _4: Iter[A]) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } fork { xs =>
            xs.take(_2).onEach(f(_)).start()
        } fork { xs =>
            xs.slice(_2, _3).replace(_4).onEach(f(_)).start()
        } fork { xs =>
            xs.drop(_3).react(f).start()
        } start()
    }
}
