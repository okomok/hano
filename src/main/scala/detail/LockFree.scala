

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.atomic


private[hano]
case class IfFirst[T](_then: T => Unit) {
    def Else(_else: T => Unit) = new _Else(_else)

    class _Else(_else: T => Unit) {
        private[this] val first = new atomic.AtomicBoolean(true)

        def apply(x: T) {
            if (first.get && first.compareAndSet(true, false)) {
                return _then(x)
            }
            _else(x)
        }

        def isSecond: Boolean = !first.get
    }
}


/**
 * Equivalent to `lazy val` with `isDone`.
 * Unlike `lazy val`, this can be recursive.
 */
@Annotation.visibleForTesting
case class CallOnce[-T](f: T => Unit) extends (T => Unit) {
    private[this] val self = IfFirst[T] { f } Else { _ => () }
    override def apply(x: T) = self(x)

    def isDone: Boolean = self.isSecond
}


@deprecated("unused")
private[hano]
class SkipFirst[-T](f: T => Unit) extends (T => Unit) {
    private[this] val self = IfFirst[T] { _ => () } Else { f }
    override def apply(x: T) = self(x)

    def isSkipped: Boolean = self.isSecond
}


@deprecated("unused")
private[hano]
class SkipTimes[-T](f: T => Unit, n: Int) extends (T => Unit) {
    private[this] val count = new atomic.AtomicInteger(n)

    override def apply(x: T) {
        var old = 0
        do {
            old = count.get
            if (old == 0) {
                return f(x)
            }
        } while (!count.compareAndSet(old, old - 1))
    }
}


@deprecated("unused")
private[hano]
class SkipWhile[-T](f: T => Unit, p: T => Boolean) extends (T => Unit) {
    @volatile private[this] var begins = false

    override def apply(x: T) {
        if (begins) {
            return f(x)
        }
        if (!p(x)) {
            begins = true
            return f(x)
        }
    }
}
