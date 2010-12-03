

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class ScanLeft[A, B](_1: Seq[A], _2: B, _3: (B, A) => B) extends Seq[B] {
    override def close() = _1.close()
    override def forloop(f: B => Unit, k: Exit => Unit) {
        var acc = _2
        f(acc)
        _1 _for { x =>
            acc = _3(acc, x)
            f(acc)
        } _andThen {
            k
        }
    }
//    override def head = _2
}

private class ScanLeft1[A, B >: A](_1: Seq[A], _3: (B, A) => B) extends Seq[B] {
    override def close() = _1.close()
    override def forloop(f: B => Unit, k: Exit => Unit) {
        var acc: Option[B] = None
        _1 _for { x =>
            if (acc.isEmpty) {
                acc = Some(x)
            } else {
                acc = Some(_3(acc.get, x))
            }
            f(acc.get)
        } _andThen {
            k
        }
    }
}