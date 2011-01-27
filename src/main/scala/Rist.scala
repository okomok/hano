

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable(single-forloop) infinite list
 */
final class Rist[A](override val context: Context = Act()) extends Resource[A] {
    require(context ne Self)
    require(context ne Unknown)

    @volatile private[this] var isActive = true
    @volatile private[this] var g: Reaction[A] = null
    private[this] val vs = new java.util.concurrent.ConcurrentLinkedQueue[A]
    private[this] val _k = detail.ExitOnce { q => g.exit(q); close() }

    override protected def closeResource() {
        isActive = false
        vs.clear()
    }
    override protected def openResource(f: Reaction[A]) {
        g = f
        while (!vs.isEmpty) {
            val v = vs.poll
            if (v != null) {
                eval(f, v)
            }
        }
    }

    def add(x: A) {
        if (isActive) {
            if (g != null) {
                eval(g, x)
            } else {
                vs.offer(x)
                if (g != null && vs.remove(x)) {
                    eval(g, x)
                }
            }
        }
    }

    @Annotation.aliasOf("add")
    def +=(x: A): Unit = add(x)

    private def eval(f: Reaction[A], x: A) {
        context onEach { _ =>
            _k.beforeExit {
                f(x)
            }
        } onExit {
            case q @ Exit.Failed(t) => _k(q)
            case _ => ()
        } start()
    }
}
