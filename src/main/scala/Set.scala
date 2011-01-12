

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable unordered set (Exit.End isn't sent.)
 */
final class Set[A](val size: Int, override val context: Context = Context.act) extends Seq[A] {
    require(context ne Context.self)

    private[this] var cur = 0
    private[this] val curLock = new java.util.concurrent.locks.ReentrantLock

    private[this] lazy val vs: Array[Val[A]] = {
        val that = new Array[Val[A]](size)
        for (i <- 0 until size) {
            that(i) = new Val[A](context)
        }
        that
    }

    override def forloop(f: Reaction[A]) {
        val _k = detail.ExitOnce { q => f.exit(q) }

        for (v <- vs) {
            v `for` { x =>
                _k.beforeExit {
                    f(x)
                }
            } exit {
                case q @ Exit.Failed(_) => _k(q)
                case q => ()
            }
        }
    }

    def empty: Set[A] = new Set[A](0, context)

    def isEmpty: Boolean = size == 0

    @Annotation.aliasOf("member")
    def +=(x: A): Boolean = member(x)

    def member(x: A): Boolean = {
        if (cur < size) {
            curLock.lock()
            val j = try {
                val tmp = cur
                cur += 1
                tmp
            } finally {
                curLock.unlock()
            }
            if (j < size) {
                vs(j) := x
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}