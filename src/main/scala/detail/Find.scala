

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Find[A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (_2(x)) {
                f(x)
                f.exit(Exit.End)
            }
        } onExit {
            case Exit.End => f.exit(Exit.Failed(new NoSuchElementException("Seq.find")))
            case q => f.exit(q)
        } start()
    }
}
