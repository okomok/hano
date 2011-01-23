

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ScanLeft[A, B](_1: Seq[A], _2: B, _3: (B, A) => B) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        var acc = _2
        context onEach { _ =>
            f(acc)
        } onExit {
            case Exit.End => {
                _1 onEach { x =>
                    acc = _3(acc, x)
                    f(acc)
                } onExit {
                    f.exit(_)
                } start()
            }
            case q => f.exit(q)
        } start()
    }
}

private[hano]
class ScanLeft1[A, B >: A](_1: Seq[A], _3: (B, A) => B) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        var acc: Option[B] = None
        _1 onEach { x =>
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
