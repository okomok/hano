

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object listen {

    sealed abstract class Env[A] extends Reaction[A] {

        /**
         * How to add a listener
         */
        def add(body: => Unit)

        /**
         * How to remove a listener
         */
        def remove(body: => Unit)
    }

    /**
     * Creates a Seq from listeners.
     */
    def apply[A](pro: Process = Unknown)(body: Env[A] => Unit): Seq[A] = new Apply(pro, body)

    /**
     * Trivial helper to define a `Seq` directly.
     */
    trait To[A] extends SeqProxy[A] {
        override lazy val self = hano.listen[A](process) { env =>
            listen(env)
        }

        override def process = Unknown.asProcess

        protected type Env = hano.listen.Env[A]
        protected def listen(env: Env)
    }


    private class Apply[A](_1: Process, _2: Env[A] => Unit) extends Seq[A] {
        Require.notSelf(_1, "listen")

        override def process = _1

        override def forloop(f: Reaction[A]) {
            val env = new EnvImpl(f)

            env.enter()
            _2(env)

            if (process ne Unknown) {
                process.invoke {
                    f.enter {
                        Exit { q =>
                            env._remove()
                            process.invoke { // wrapped for thread-safety
                                f.fail(Exit.Interrupted(q))
                            }
                        }
                    }
                }
            }

            env._add()
        }

        private class EnvImpl[A](f: Reaction[A]) extends Env[A] {
            private[hano] var _add = new detail.VarFunc0
            private[hano] var _remove = new detail.VarFunc0

            override protected def rawEnter(p: Exit) = ()
            override protected def rawApply(x: A) {
                f.enter {
                    Exit { q =>
                        _remove()
                        if (process ne Unknown) {
                            process.invoke {
                                f.fail(Exit.Interrupted(q))
                            }
                        }
                    }
                } applying {
                    f(x)
                }
            }
            override protected def rawExit(q: Exit.Status) = f.exit(q)

            override def add(body: => Unit) {
                _add := body
            }
            override def remove(body: => Unit) {
                _remove := body
            }
        }
    }
}
