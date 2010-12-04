


// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.util.continuations.{suspendable, reset, shift}


object CpsGenerator {

    def apply[A](op: (A => Unit @suspendable) => Unit @suspendable) = new scala.collection.Iterable[A] {
        override def iterator = new CursorImpl(op).toSIterator
    }

    private class CursorImpl[A](_1: (A => Unit @suspendable) => Unit @suspendable) extends Cursor[A] {
        private[this] var _e: Option[A] = None
        private[this] var _k: Unit => Unit = null
        private[this] val _y = new (A => Unit @suspendable) {
            override def apply(e: A) = {
                _e = Some(e)
                _suspend()
            }
        }

        reset {
            _suspend()
            _1(_y)
        }
        _k()

        override def isEnd = _e.isEmpty
        override def deref = _e.get
        override def increment() {
            _e = None
            _k()
        }

        private def _suspend(): Unit @suspendable = shift { (k: Unit => Unit) => _k = k }
    }

}
