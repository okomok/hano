

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable(single-forloop) infinite list
 */
final class Rist[A](override val context: Context = async) extends SeqOnce[A] with java.io.Closeable {
    require(context ne Self)
    require(context ne Unknown)

    @volatile private[this] var isActive = true
    @volatile private[this] var g: Reaction[A] = null
    private[this] val vs = new java.util.concurrent.ConcurrentLinkedQueue[A]
    private def _k(q: Exit) { close(); g.exit(q) }

    override def close() {
        isActive = false
        vs.clear()
    }
    override protected def forloopOnce(f: Reaction[A]) {
        g = f
        while (!vs.isEmpty) {
            val v = vs.poll
            if (v != null) {
                _eval(f, v)
            }
        }
    }

    def add(x: A) {
        if (isActive) {
            if (g != null) {
                _eval(g, x)
            } else {
                vs.offer(x)
                if (g != null && vs.remove(x)) {
                    _eval(g, x)
                }
            }
        }
    }

    @annotation.aliasOf("add")
    def +=(x: A): Unit = add(x)

    private def _eval(f: Reaction[A], x: A) {
        context onEach { _ =>
            g beforeExit {
                f.enter()
                f(x)
            }
        } onExit {
            case q @ Exit.Failed(_) => _k(q)
            case _ => ()
        } start()
    }
}
