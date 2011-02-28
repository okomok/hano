

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FoldLeft[A, B](_1: Seq[A], _2: B, _3: (B, A) => B) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        var acc = _2

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            acc = _3(acc, x)
        } onExit {
            case Exit.Success => f(acc)
            case q => f.exit(q)
        } start()
    }
}


private[hano]
class ReduceLeft[A, B >: A](_1: Seq[A], _3: (B, A) => B) extends SeqAdapter.Of[B](_1) {
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
        } onExit {
            case Exit.Success => {
                if (acc.isEmpty) {
                    f.exit(Exit.Failure(new NoSuchElementException("Seq.reduceLeft")))
                } else {
                    f(acc.get)
                }
            }
            case q => f.exit(q)
        } start()
    }
}
