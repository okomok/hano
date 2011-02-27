

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


@annotation.pre("`c` shall be thread-safe.")
final class Entrance(c: () => Unit) extends java.io.Closeable {
    private[this] lazy val _close = c()

    @annotation.threadSafe @annotation.idempotent
    override def close() = _close
}


object Entrance {
    val Nil = new Entrance(() => ())

    def apply[U](c: => U)(implicit d: DummyImplicit) = new Entrance(() => c)

    private[hano]
    def second(p: Entrance): Entrance = {
        val np = detail.IfFirst[Unit] { _ => () } Else { _ => p.close() }
        Entrance {
            np()
        }
    }

    private[hano]
    class Two(_1: Reaction[_]) extends (Entrance => Unit) {
        private[this] var en1, en2 = Entrance.Nil
        override def apply(p: Entrance) {
            if (_1.isExited) {
                p.close()
            } else if (_1.isEntered) {
                en2 = p
            } else {
                en1 = p
                _1.enter {
                    en1.close()
                    en2.close()
                }
            }
        }
    }
}
