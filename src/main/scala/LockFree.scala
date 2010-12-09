

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.atomic


@Annotation.visibleForTesting
case class IfFirst[T](_then: T => Unit) {
    def Else(_else: T => Unit): _IfFirst[T] = new _IfFirst[T](_then, _else)
}

@Annotation.visibleForTesting
class _IfFirst[-T](_then: T => Unit, _else: T => Unit) extends Function1[T, Unit] {
    private[this] val first = new atomic.AtomicBoolean(true)

    override def apply(x: T) {
        if (first.get && first.compareAndSet(true, false)) {
            return _then(x)
        }
        _else(x)
    }

    def isSecond: Boolean = !first.get
}


/**
 * Equivalent to `lazy val` with `isDone`.
 * Unlike `lazy val`, this can be recursive.
 */
@Annotation.visibleForTesting
case class CallOnce[-T](f: T => Unit) extends Function1[T, Unit] {
    private[this] val delegate = new _IfFirst[T](f, _ => ())
    override def apply(x: T) = delegate(x)

    def isDone: Boolean = delegate.isSecond
}


@deprecated("unused")
private class SkipFirst[-T](f: T => Unit) extends Function1[T, Unit] {
    private[this] val delegate = new _IfFirst[T](_ => (), f)
    override def apply(x: T) = delegate(x)

    def isSkipped: Boolean = delegate.isSecond
}


@deprecated("unused")
private class SkipTimes[-T](f: T => Unit, n: Int) extends Function1[T, Unit] {
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
private class SkipWhile[-T](f: T => Unit, p: T => Boolean) extends Function1[T, Unit] {
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
