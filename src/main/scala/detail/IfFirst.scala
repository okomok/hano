

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object IfFirst {
    def apply[T](_then: T => Unit) = new IfFirst(_then)
}

private[hano]
class IfFirst[T](_then: T => Unit) {
    def Else(_else: T => Unit) = new _Else(_else)

    class _Else(_else: T => Unit) {
        private[this] val first = new java.util.concurrent.atomic.AtomicBoolean(true)

        def apply(x: T) {
            if (first.get && first.compareAndSet(true, false)) {
                _then(x)
            } else {
                _else(x)
            }
        }

        def isSecond: Boolean = !first.get

        def toFunction: T => Unit = x => apply(x)
    }
}
