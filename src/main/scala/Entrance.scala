

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait Entrance extends java.io.Closeable {
    @annotation.threadSafe
    protected def rawClose(q: Exit)

    @annotation.threadSafe @annotation.idempotent
    final def close(q: Exit): Unit = _close(q)

    @annotation.equivalentTo("close(Exit.End)")
    final override def close(): Unit = close(Exit.End)

    private[this] val _close = detail.IfFirst[Exit] { q => rawClose(q) } Else { _ => () }

    final def second: Entrance = new Entrance.Second(this)
}


object Entrance {

    def apply(k: Exit => Unit): Entrance = new Apply(k)

    object Nil extends Entrance {
        override protected def rawClose(q: Exit) = ()
    }

    private class Apply(_1: Exit => Unit) extends Entrance {
        override protected def rawClose(q: Exit) = _1(q)
    }

    private class Second(_1: Entrance) extends Entrance {
        private[this] val _c = detail.IfFirst[Exit] { _ => () } Else { q => _1.close(q) }
        override protected def rawClose(q: Exit) = _c(q)
    }

    private[hano]
    class Two(_1: Reaction[_]) extends (Entrance => Unit) {
        private[this] var en1, en2: Entrance = Nil

        override def apply(p: Entrance) {
            if (_1.isExited) {
                p.close()
            } else if (_1.isEntered) {
                en2 = p
            } else {
                en1 = p
                _1.enter {
                    Entrance { _ =>
                        en1.close()
                        en2.close()
                    }
                }
            }
        }
    }
}
