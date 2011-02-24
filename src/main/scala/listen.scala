

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object listen {

    sealed abstract class Env[A] extends Reaction[A] {
        /**
         * How to add a listener
         */
        def addBy(add: => Unit): Unit

        /**
         * How to remove a listener
         */
        def removeBy(remove: => Unit): Unit

        /**
         * Where to evaluate a reaction (`Unknown` if omitted.)
         */
        def contextIs(cxt: Context): Unit
    }

    /**
     * Creates a Seq from listeners.
     */
    def apply[A](body: Env[A] => Unit): Seq[A] = new Apply(body)

    private class Apply[A](body: Env[A] => Unit) extends SeqResource[A] {

        private[this] var _f: Reaction[A] = null
        private[this] var _add: () => Unit = null
        private[this] var _remove: () => Unit = null
        private[this] var _context: Context = Unknown
        private[this] val _env = new EnvImpl

        private[this] var _firstTime = true
        body(_env) // saves context as soon as possible.

        override def context = _context

        override protected def closeResource() {
            if (_remove != null) {
                _remove()
            }
        }

        override protected def openResource(f: Reaction[A]) {
            if (_firstTime) {
                _firstTime = false
            } else {
                body(_env)
            }
            _f = f
            if (_add != null) {
                _add()
            }
        }

        private class EnvImpl extends Env[A] {

            override protected def rawApply(x: A) = _f(x)
            override protected def rawExit(q: Exit) = _f.exit(q)

            override def addBy(add: => Unit) {
                _add = () => add
            }
            override def removeBy(remove: => Unit) {
                _remove = () => remove
            }
            override def contextIs(ctx: Context) {
                _context = ctx
            }
        }
    }
}
