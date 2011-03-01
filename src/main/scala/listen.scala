

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object listen {

    sealed abstract class Env[A] extends Reaction[A] {
        /**
         * How to add a listener
         */
        def addBy(body: => Unit)

        /**
         * How to remove a listener
         */
        def removeBy(body: => Unit)

        /**
         * Where to evaluate a reaction (`Unknown` if omitted.)
         */
        def contextIs(cxt: Context)
    }

    /**
     * Creates a Seq from listeners.
     */
    def apply[A](body: Env[A] => Unit): Seq[A] = new Apply(body)

    private class Apply[A](body: Env[A] => Unit) extends Seq[A] {
        private[this] var _f: Reaction[A] = null
        private[this] var _add: () => Unit = null
        private[this] var _remove: () => Unit = null
        private[this] var _context: Context = Unknown

        body(_makeEnv) // saves context as soon as possible.
        private[this] val _evalBody = detail.IfFirst[Unit] { _ => () } Else { _ => body(_makeEnv) }

        private def _makeEnv = {
            val that = new EnvImpl
            that.enter()
            that
        }

        override def context = _context
        override def forloop(f: Reaction[A]) {
            _evalBody()
            _f = f

            if (_context ne Unknown) {
                _context.eval {
                    _f.enter {
                        Exit { q =>
                            if (_remove != null) {
                                _remove()
                            }
                            _context.eval { // wrapped for thread-safety
                                _f.exit(q)
                            }
                        }
                    }
                }
            }

            if (_add != null) {
                _add()
            }
        }

        private class EnvImpl extends Env[A] {
            override protected def rawEnter(p: Exit) = ()
            override protected def rawApply(x: A) {
                _f.enter {
                    Exit { q =>
                        if (_remove != null) {
                            _remove()
                        }
                        if (_context ne Unknown) {
                            _context.eval {
                                _f.exit(q)
                            }
                        }
                    }
                }
                _f(x)
            }
            override protected def rawExit(q: Exit.Status) = _f.exit(q)

            override def addBy(body: => Unit) {
                _add = () => body
            }
            override def removeBy(body: => Unit) {
                _remove = () => body
            }
            override def contextIs(ctx: Context) {
                _context = ctx
            }
        }
    }
}
