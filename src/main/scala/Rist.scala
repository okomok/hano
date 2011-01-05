

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable list
 */
final class Rist[A] extends Seq[A] with CheckedReaction[A] {
    private[this] val _xs = new java.util.ArrayList[A]
    private[this] var _q: Exit = null
    private[this] val _fs = new java.util.ArrayList[Reaction[A]]

    @Annotation.equivalentTo("apply(x)")
    final def add(x: A): Unit = apply(x)

    @Annotation.equivalentTo("it.able.foreach(add(_))")
    final def addAll(it: Iter[A]): Unit = it.able.foreach(add(_))

    @Annotation.equivalentTo("add(x)")
    final def +=(x: A): Unit = add(x)

    @Annotation.equivalentTo("addAll(it)")
    final def ++=(it: Iter[A]): Unit = addAll(it)

    override def forloop(f: Reaction[A]) {
        // TODO
        f.tryRethrow(context) {
            for (x <- Iter.from(_xs).able) {
                f(x)
            }
            if (_q == null) {
                _fs.add(new detail.WrappedReaction(f))
            }
        }

        if (_q != null) {
            f.exit(_q)
        }
    }

    override protected def checkedApply(x: A) {
        _xs.add(x)

        val errs = new java.util.ArrayList[Reaction[A]]
        var s: Throwable = null // TODO: set of exceptions?
        for (f <- Iter.from(_fs).able) {
            try {
                f.tryRethrow(context) {
                    f(x)
                }
            } catch {
                case t: Throwable => {
                    errs.add(f)
                    s = t
                }
            }
        }

        for (f <- Iter.from(errs).able) {
            _fs.remove(f)
        }
        if (s != null) {
            throw s
        }
    }

    override protected def checkedExit(q: Exit) {
        _q = q

        for (f <- Iter.from(_fs).able) {
            try {
                f.exit(q)
            } catch {
                case t: Throwable =>
            }
        }

        _fs.clear()
    }
}


object Rist {
    /**
     * Constructs a Rist with initial values.
     */
    def apply[A](inits: A*): Rist[A] = {
        val that = new Rist[A]
        that.addAll(inits)
        that
    }
}
