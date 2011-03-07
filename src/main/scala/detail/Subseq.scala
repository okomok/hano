

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Subseq[A, B](_1: Seq[A], _2: Iter[Int]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val it = _2.ator
        var i, j = 0

        _1.onEnter { p =>
            f.enter(p)
            if (!it.hasNext) {
                f.exit(Exit.Success)
            } else {
                j = it.next
            }
        } onEach { x =>
            f.beforeExit {
                if (i == j) {
                    f(x)
                    if (it.hasNext) {
                        val oldj = j
                        j = it.next
                        require(oldj < j, "`subseq` requires a strictly-increasing `Iterator`")
                    } else {
                        f.exit(Exit.Success)
                    }
                }
                i += 1
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
