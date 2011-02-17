

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Replace[A](_1: Seq[A], _2: Iter[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        val it = _2.ator
        _1 onEach { x =>
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
class ReplaceRegion[A](_1: Seq[A], _2: Int, _3: Int, _4: Iter[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) =
        _1.fork{ _.take(_2).onEach(f(_)).start() }.
           fork{ _.slice(_2, _3).replace(_4).onEach(f(_)).start() }.
           fork{ _.drop(_3).react(f).start() }.
           start()
}
