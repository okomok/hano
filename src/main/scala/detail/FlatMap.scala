

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// No End sequence
private[hano]
class FlatMap[A, B](_1: Seq[A], _2: A => Seq[B]) extends Seq[B] {
    override def close() = _1.close()
    override val context = _1.context.known
    override def forloop(f: Reaction[B]) {
        def _k(q: Exit) { close(); f.exit(q) }

        _1 onEach { x =>
            f beforeExit {
                _2(x).shift(_1) onEach {
                    f(_)
                } onExit {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ => ()
                } start()
            }
        } onExit {
            case q @ Exit.Failed(_) => _k(q)
            case _ => ()
        } start()
    }
}
