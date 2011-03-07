

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ScanLeft[A, B](_1: Seq[A], _2: B, _3: (B, A) => B) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        var acc = _2

        _1.onEnter { p =>
            f.enter(p)
            f(acc)
        } onEach { x =>
            acc = _3(acc, x)
            f(acc)
        } onExit {
            f.exit(_)
        } start()
    }
}


private[hano]
class ScanLeft1[A, B >: A](_1: Seq[A], _3: (B, A) => B) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        var acc: Option[B] = None

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (acc.isEmpty) {
                acc = Some(x)
            } else {
                acc = Some(_3(acc.get, x))
            }
            f(acc.get)
        } onExit {
            f.exit(_)
        } start()
    }
}
