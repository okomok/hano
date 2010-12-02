

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Catching[A](_1: Seq[A], _2: PartialFunction[Throwable, Unit]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        _1 _for { x =>
            try {
                f(x)
            } catch {
                case t: Throwable => {
                    if (_2.isDefinedAt(t)) {
                        _2(t)
                    } else {
                        throw t
                    }
                }
            }
        } _andThen {
            k
        }
    }
}
