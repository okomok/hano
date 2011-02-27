

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// See:
//   http://jim-mcbeath.blogspot.com/2010/11/nondeterministic-evaluation-in-scala.html


import scala.util.continuations.{cpsParam, suspendable, reset, shift}


private[hano]
class CpsIterable[A](_1: generator.cps.Env[A] => Any @suspendable) extends Iterable[A] {
    override def iterator = {
        import CpsIterable._
        new IteratorImpl(_1).concrete
    }
}


private[hano]
object CpsIterable {
    import generator.cps.Env

    private class IteratorImpl[A](body: Env[A] => Any @suspendable) extends AbstractIterator[A] {
        private[this] var _x: Option[A] = None
        private[this] var _k: Unit => Unit = null
        private[this] val _y = new Env[A] {
            override def apply(x: A) = {
                _x = Some(x)
                _suspend()
            }
            override def amb[B](xs: Iter[B]): B @cpsParam[Any, Unit] = {
                shift { k: (B => Any) =>
                    val it = xs.ator
                    reset {
                        while (it.hasNext) {
                            k(it.next)
                            _traverse()
                        }
                    }
                }
            }
        }

        reset {
            _suspend()
            body(_y)
            ()
        }
        _k()

        override def isEnd = _x.isEmpty
        override def deref = _x.get
        override def increment() {
            _x = None
            _k()
        }

        private def _suspend(): Unit @suspendable = shift { (k: Unit => Unit) => _k = k }

        private def _traverse(): Unit @suspendable = {
            var k: Unit => Unit = null
            while (!isEnd) {
                k = _k
                _suspend()
                k()
            }
        }
    }
}
