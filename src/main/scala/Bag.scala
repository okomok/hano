

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable infinite unordered multi-set
 */
final class Bag[A](val capacity: Int, override val context: Context = async) extends Seq[A] {
    require(context ne Self)
    require(context ne Unknown)

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
        for (v <- vs) {
            v onEach {
                f(_)
            } onExit {
                case q @ Exit.Failed(_) => f.exit(q)
                case q => ()
            } start()
        }
    }

    @annotation.aliasOf("member")
    def +=(x: A): Boolean = member(x)

    def member(x: A): Boolean = {
        if (cur < capacity) {
            val j = detail.Synchronized(curLock) {
                val tmp = cur
                cur += 1
                tmp
            }
            if (j < capacity) {
                vs(j)() = x
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
