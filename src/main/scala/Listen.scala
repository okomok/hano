

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Listen {

    sealed abstract class Env[A] extends Reaction[A] {
        /**
         * How to add a listener
         */
        def addBy(add: => Unit): Unit

        /**
         * How to remove a listener
         */
        def removeBy(remove: => Unit): Unit
    }

    /**
     * Creates a Seq from listeners.
     */
    def apply[A](ctx: Context = Context.unknown)(body: Env[A] => Unit): Seq[A] = {
        val env = new EnvImpl[A](ctx)
        body(env)
        env.asSeq
    }

    private class EnvImpl[A](ctx: Context) extends Env[A] with CheckedReaction[A] {
        @volatile private[this] var _f: Reaction[A] = null
        private[this] var _add: () => Unit = null
        private[this] var _remove: () => Unit = null

        override def checkedApply(x: A) = _f(x)
        override def checkedExit(q: Exit) = _f.exit(q)

        override def addBy(add: => Unit) {
            _add = () => add
        }
        override def removeBy(remove: => Unit) {
            _remove = () => remove
        }

        lazy val asSeq: Seq[A] = new Resource[A] {
            override def context = ctx
            override protected def closeResource() {
                if (_remove != null) {
                    _remove()
                }
                _remove = null
            }
            override protected def openResource(f: Reaction[A]) {
                _f = f // synchronization point
                if (_add != null) {
                    _add()
                }
                _add = null
            }
        }
    }
}
