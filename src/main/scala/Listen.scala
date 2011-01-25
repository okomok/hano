

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Listen {

    sealed abstract class Env[A] extends Reaction[A] {
        def addBy(add: => Unit): Unit
        def removeBy(remove: => Unit): Unit
    }

    def apply[A](ctx: Context = Context.act)(body: Env[A] => Unit): Seq[A] = {
        val env = new EnvImpl[A](ctx)
        body(env)
        env.toSeq
    }

    private class EnvImpl[A](ctx: Context) extends Env[A] with CheckedReaction[A] {

        @volatile private[this] var _f: Reaction[A] = null
        @volatile private[this] var _add: () => Unit = null
        @volatile private[this] var _remove: () => Unit = null

        override def checkedApply(x: A) {
            ctx.eval {
                _f(x)
            }
        }
        override def checkedExit(q: Exit) {
        }

        override def addBy(add: => Unit) {
            _add = () => add
        }
        override def removeBy(remove: => Unit) {
            _remove = () => remove
        }

        def toSeq: Seq[A] = new Resource[A] {
            assert(_add != null)
            assert(_remove != null)
            override def context = ctx
            override protected def closeResource() = _remove()
            override protected def openResource(f: Reaction[A]) {
                _f = f
                _add()
            }
        }
    }
}
