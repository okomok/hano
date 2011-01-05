

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Take[A](_1: Seq[A], _2: Int) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        val _k = CallOnce[Exit] { q => f.exit(q);close() }

        if (_2 == 0) {
            context.eval {
                _k(Exit.End)
            }
        } else {
            var c = _2
            For(_1) { x =>
                if (c != 0) {
                    f(x)
                    c -= 1
                    if (c == 0) {
                        _k(Exit.End)
                    }
                }
            } AndThen {
                _k(_)
            }
        }
    }
    override def take(n: Int): Seq[A] = _1.take(java.lang.Math.min(_2, n)) // take.take fusion
}
