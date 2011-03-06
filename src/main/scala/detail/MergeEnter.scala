

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class MergeEnter(f: Reaction[_], ctx: Context) extends (Exit => Unit) {
    private[this] var _exit1, _exit2: Exit = Exit.Empty

    override def apply(p: Exit) {
        if (f.isExited) {
            p(Exit.Failure(break.Control))
        } else if (f.isEntered) {
            _exit2 = p
        } else {
            _exit1 = p
            f.enter {
                Exit { q =>
                    ctx.eval { // for thread-safety
                        _exit1(q)
                        _exit2(q)
                        f.exit(Exit.Failure(Exit.ByOther(q)))
                    }
                }
            }
        }
    }
}
