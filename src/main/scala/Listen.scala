

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ArrayList


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
    def apply[A](ctx: Context = Context.act)(body: Env[A] => Unit): Seq[A] = {
        val env = new EnvImpl[A](ctx)
        body(env)
        env.asSeq
    }

    private class EnvImpl[A](ctx: Context) extends Env[A] with CheckedReaction[A] {
        @volatile private[this] var _f: Reaction[A] = null
        private[this] var _adds = new ArrayList[() => Unit]
        private[this] var _removes = new ArrayList[() => Unit]
        private[this] val _k = detail.ExitOnce { q => _f.exit(q); asSeq.close() }

        override def checkedApply(x: A) {
            ctx onEach { _ =>
                _k.beforeExit {
                    _f(x)
                }
            } onExit {
                case q @ Exit.Failed(t) => _k(q)
                case _ => ()
            } start()
        }
        override def checkedExit(q: Exit) {
            asSeq.close()
        }

        override def addBy(add: => Unit) {
            _adds.add(() => add)
        }
        override def removeBy(remove: => Unit) {
            _removes.add(() => remove)
        }

        lazy val asSeq: Seq[A] = new Resource[A] {
            override def context = ctx
            override protected def closeResource() {
                for (remove <- Iter.from(_removes).able) {
                    remove()
                }
                _removes = null
            }
            override protected def openResource(f: Reaction[A]) {
                _f = f // synchronization point
                for (add <- Iter.from(_adds).able) {
                    add()
                }
                _adds = null
            }
        }
    }
}
