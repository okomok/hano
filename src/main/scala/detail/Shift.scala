

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        val _k = CallOnce[Exit] { q => f.exit(q);close() }

        For(_1) { x =>
            For(context) { _ =>
                if (!_k.isDone) {
                    f(x)
                }
            } AndThen {
                case q @ Exit.Failed(_) => _k(q)
                case _ =>
            }
        } AndThen { q =>
            context.eval {
                _k(q)
            }
        }
    }
}
