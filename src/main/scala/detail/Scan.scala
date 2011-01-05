

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
        For(context) { _ =>
            f(acc)
        } AndThen {
            case Exit.End => {
                For(_1) { x =>
                    acc = _3(acc, x)
                    f(acc)
                } AndThen {
                    f.exit(_)
                }
            }
            case q => f.exit(q)
        }
    }
}

private[hano]
class ScanLeft1[A, B >: A](_1: Seq[A], _3: (B, A) => B) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        var acc: Option[B] = None
        For(_1) { x =>
            if (acc.isEmpty) {
                acc = Some(x)
            } else {
                acc = Some(_3(acc.get, x))
            }
            f(acc.get)
        } AndThen {
            f.exit(_)
        }
    }
}
