

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent


object Val {

    /**
     * Returns a Val with initial value.
     */
    def apply[A](x: A): Val[A] = {
        val v = new Val[A]
        v := x
        v
    }

    @Annotation.equivalentTo("new Val[A]")
    def apply[A]: Val[A] = new Val[A]
}


/**
 * Single-assignment value
 */
final class Val[A](override val context: Context = Context.act) extends Seq[A] {
    require(context ne Context.self)

    private[this] val v = new concurrent.atomic.AtomicReference[Option[A]](None)
    private[this] val fs = new concurrent.ConcurrentLinkedQueue[Reaction[A]]

    // subscription order is NOT preserved.
    override def forloop(f: Reaction[A]) {
        if (!v.get.isEmpty) {
            eval(f, v.get.get)
        } else {
            fs.offer(f)
            if (!v.get.isEmpty && fs.remove(f)) {
                eval(f, v.get.get)
            }
        }
    }

    def :=(x: A) {
        if (v.compareAndSet(None, Some(x))) {
            while (!fs.isEmpty) {
                val f = fs.poll
                if (f != null) {
                    eval(f, x)
                }
            }
        } else if (v.get.get != x) {
            throw new MultipleAssignmentException(v.get.get, x)
        }
    }

    private def eval(f: Reaction[A], x: A) {
        context onEach { _ =>
            f(x)
        } onExit {
            f.exit(_)
        } start()
    }
}


class MultipleAssignmentException[A](expected: A, actual: A) extends
    RuntimeException("expected: " + expected + ", but actual: " + actual)
