

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Find[A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        def _k(q: Exit) { close(); f.exit(q) }

        _1 onEach { x =>
            if (_2(x)) {
                f(x)
                _k(Exit.End)
            }
        } onExit {
            case Exit.End => _k(Exit.Failed(new NoSuchElementException("Seq.find")))
            case q => _k(q)
        } start()
    }
}
