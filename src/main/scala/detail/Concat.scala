

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ConcatIf[A](_1: Iter[Seq[A]], _2: Context, cond: Exit.Status => Boolean) extends Seq[A] {
    override def context = _2

    override def forloop(f: Reaction[A]) {

        def rec(it: Iterator[Seq[A]]) {
            it.next.shift {
                context
            } onEnter { p =>
                f.enter {
                    Exit { q =>
                        p(q)
                        context.eval { // for thread-safety
                            f.exit(Exit.Failure(Exit.ByOther(q)))
                        }
                    }
                }
            } onEach {
                f(_)
            } onExit { q =>
                f.beforeExit {
                    if (cond(q)) {
                        if (it.hasNext) {
                            rec(it)
                        } else {
                            f.exit(Exit.Success)
                        }
                    } else {
                        f.exit(q)
                    }
                }
            } start()
        }

        val it = _1.ator

        if (!it.hasNext) {
            context.eval {
                f.exit(Exit.Success)
            }
        } else {
            rec(it)
        }
    }
}
