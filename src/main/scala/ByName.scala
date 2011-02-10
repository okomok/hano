

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * By-name sequence
 */
case class ByName[A](_1: () => Seq[A]) extends Seq[A] {
    override def context = _head.context
    override def forloop(f: Reaction[A]) = _forloop(f)

    private[this] lazy val _head = _1()
    private[this] val _forloop = {
        detail.IfFirst[Reaction[A]] { f =>
            _head.forloop(f)
        } Else { f =>
            _1().forloop(f)
        }
    }
}

object ByName {
    def apply[A](body: => Seq[A])(implicit d: DummyImplicit): Seq[A] = new ByName(() => body)
}
