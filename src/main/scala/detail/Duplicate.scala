

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Duplicate[A](_1: Seq[A]) extends Seq[A] {
    private[this] var _f: Reaction[A] = null
    private[this] val _close = IfFirst[Unit] { _ => () } Else { _ => _1.close() }
    private[this] val _forloop = {
        IfFirst[Reaction[A]] { f =>
            _f = f
        } Else { f =>
            _1.react(_f).forloop(f)
        }
    }
    override def close() = _close()
    override def forloop(f: Reaction[A]) = _forloop(f)
}
