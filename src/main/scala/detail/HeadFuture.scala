

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class HeadFuture[A](_1: Seq[A]) extends (() => A) {
    private[this] var _x: Option[A] = None
    private[this] var _q: Exit.Status = null
    private[this] val _c = new java.util.concurrent.CountDownLatch(1)

    _1.head.onEach { x =>
        CountDown(_c) {
            _x = Some(x)
        }
    } onExit { q =>
        CountDown(_c) {
            _q = q
        }
    } start()

    override def apply(): A = {
        _c.await()

        if (_x.isEmpty) {
            assert(_q ne null)
            _q match {
                case Exit.Failure(t) => throw t
                case _ => assert(_x.isDefined); _x.get
            }
        } else {
            _x.get
        }
    }
}
