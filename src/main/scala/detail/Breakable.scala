

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Breakable[A](_1: Seq[A]) extends Seq[(A, Function0[Unit])] {
    override def close() = _1.close()
    override def forloop(f: Reaction[(A, Function0[Unit])]) {
        val _k = CallOnce[Exit] { q => f.onExit(q);close() }

        For(_1) { x =>
            f(x, () => _k(Exit.End))
        } AndThen { q =>
            _k(q)
        }
    }
}