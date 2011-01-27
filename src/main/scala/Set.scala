

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable infinite unordered set
 */
final class Set[A](val capacity: Int, override val context: Context = Context.act) extends Seq[A] {
    require(context ne Context.self)
    require(context ne Context.unknown)

    private[this] var cur = 0
    private[this] val curLock = new java.util.concurrent.locks.ReentrantLock

    private[this] lazy val vs: Array[Val[A]] = {
        val that = new Array[Val[A]](capacity)
        for (i <- 0 until capacity) {
            that(i) = new Val[A](context)
        }
        that
    }

    override def forloop(f: Reaction[A]) {
        val _k = detail.ExitOnce { q => f.exit(q) }

        for (v <- vs) {
            v onEach { x =>
                _k.beforeExit {
                    f(x)
                }
            } onExit {
                case q @ Exit.Failed(_) => _k(q)
                case q => ()
            } start()
        }
    }

    @Annotation.aliasOf("member")
    def +=(x: A): Boolean = member(x)

    def member(x: A): Boolean = {
        if (cur < capacity) {
            val j = detail.Synchronized(curLock) {
                val tmp = cur
                cur += 1
                tmp
            }
            if (j < capacity) {
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
