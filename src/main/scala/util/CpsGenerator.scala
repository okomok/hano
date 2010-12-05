

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.util.continuations.{suspendable, reset, shift}


object CpsGenerator {

    def apply[A](body: Env[A] => Unit @suspendable) = new Iterable[A] {
        override def iterator = new CursorImpl(body).toIterator
    }

    sealed abstract class Env[-A] extends hano.Block.Env {
        def apply(x: A): Unit @suspendable
        def amb[A](xs: Iter[A]): A @suspendable
    }

    private class CursorImpl[A](body: Env[A] => Unit @suspendable) extends Cursor[A] {
        private[this] var _x: Option[A] = None
        private[this] var _k: Unit => Unit = null
        private[this] val _y = new Env[A] {
            override def apply(x: A) = {
                _x = Some(x)
                _saveK()
            }
            override def amb[A](xs: Iter[A]): A @suspendable = {
                shift { k: (A => Unit) =>
                    val it = xs.begin
                    reset[Unit, Unit] {
                        var x: A = null.asInstanceOf[A]
                        while (it.hasNext) {
                            x = it.next
                            k(x)
                            _incrementAll
                        }
                    }
                }
            }
        }

        reset {
            _saveK()
            body(_y)
        }
        _k()

        override def isEnd = _x.isEmpty
        override def deref = _x.get
        override def increment() {
            _x = None
            _k()
        }

        private def _saveK(): Unit @suspendable = shift { (k: Unit => Unit) => _k = k }

        private def _incrementAll(): Unit @suspendable = {
            var k: Unit => Unit = null
            while (!isEnd) {
                k = _k
                _saveK()
                k()
            }
        }
    }
}
