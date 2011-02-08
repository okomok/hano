

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * By-name sequence
 */
case class ByName[A](_1: () => Seq[A]) extends SeqProxy[A] {
    override def self = _1()
}

object ByName {
    def apply[A](body: => Seq[A])(implicit d: DummyImplicit): Seq[A] = new ByName(() => body)
}
