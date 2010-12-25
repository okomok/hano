

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Duplicate[A](_1: Seq[A]) extends Seq[A] {
    private[this] var _fk: (A => Unit, Exit => Unit) = null
    private[this] val _close = IfFirst[Unit] { _ => () } Else { _ => _1.close() }
    private[this] val _forloop = {
        IfFirst[(A => Unit, Exit => Unit)] {
            case (f, k) => _fk = (f, k)
        } Else {
            case (f, k) => _1.react(_fk._1).onExit(_fk._2).forloop(f, k)
        }
    }
    override def close() = _close()
    override def forloop(f: A => Unit, k: Exit => Unit) = _forloop(f, k)
}
