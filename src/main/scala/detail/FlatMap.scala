

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// Exit.End is indeterminate.
private[hano]
class FlatMap[A, B](_1: Seq[A], _2: A => Seq[B]) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        val _k = ExitOnce { q => f.exit(q); close() }

        _1 `for` { x =>
            _k.beforeExit {
                _2(x).shift(_1) `for` {
                    f(_)
                } exit {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ => ()
                }
            }
        } exit {
            case q @ Exit.Failed(_) => _k(q)
            case _ => ()
        }
    }
}
