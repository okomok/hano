

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable bounded queue
 */
final class BoundedQueue[A](val capacity: Int, override val context: Context = Context.act) extends Seq[A] {
    require(context ne Context.self)

    private[this] var cur = 0
    private[this] val curLock = new java.util.concurrent.locks.ReentrantLock

    private[this] val vs = new Array[Val[A]](capacity)
    for (i <- 0 until capacity) {
        vs(i) = new Val[A](context)
    }

    override def forloop(f: Reaction[A]) {
        val _k = detail.ExitOnce { q => f.exit(q) }
        val byFail: Exit => Unit = {
            case q @ Exit.Failed(_) => _k(q)
            case q => ()
        }

        var i = 0
        for (v <- vs) {
            i += 1
            val k = if (i == capacity) { q => _k(q) } else byFail
            v `for` { x =>
                _k.beforeExit {
                    f(x)
                }
            } exit {
                k
            }
        }
    }

    def offer(x: A): Boolean = {
        if (cur < capacity) {
            curLock.lock()
            val j = try {
                val tmp = cur
                cur += 1
                tmp
            } finally {
                curLock.unlock()
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
