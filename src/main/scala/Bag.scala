

// Copyright Shunsuke Sogame 2010-2011.
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
            v onEnter {
                f.enter(_)
            } onEach {
                f(_)
            } onExit {
                case q @ Exit.Failure(_) => f.exit(q)
                case q => ()
            } start()
        }
    }

    /**
     * Adds the specified element to this set.
     */
    def add(x: A): Boolean = _next match {
        case Some(v) => {
            val ok = v.set(x)
            assert(ok)
            true
        }
        case None => false
    }

    /**
     * Adds the specified element as single-element sequence to this set.
     */
    def member(x: Seq[A]): Boolean = _next match {
        case Some(v) => {
            v.assign(x)
            true
        }
        case None => false
    }

    @annotation.aliasOf("add")
    def +=(x: A): Boolean = add(x)

    private def _next: Option[Val[A]] = {
        if (cur < capacity) {
            val j = detail.Synchronized(curLock) {
                val that = cur
                cur += 1
                that
            }
            if (j < capacity) {
                Some(vs(j))
            } else {
                None
            }
        } else {
            None
        }
    }
}
