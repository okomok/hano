

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ConcatIf[A](_1: Iter[Seq[A]], _2: Process, cond: Exit.Status => Boolean) extends Seq[A] {
    override def process = _2

    override def forloop(f: Reaction[A]) {

        def rec(it: Iterator[Seq[A]]) {
            it.next.shift {
                process
            } onEnter { p =>
                f.enter {
                    Exit { q =>
                        p(q)
                        process.eval { // for thread-safety
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
            process.eval {
                f.exit(Exit.Success)
            }
        } else {
            rec(it)
        }
    }
}