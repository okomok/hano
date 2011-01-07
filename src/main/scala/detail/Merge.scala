

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def context = _1.context upper _2.context
    override def forloop(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q) }
        val _ok = IfFirst[Exit] { _ => () } Else { q => _k(q) }
        val _no = ExitOnce { q => _k(q);close() }

        For(_1.shift(context)) { x =>
            _no.beforeExit {
                f(x)
            }
        } AndThen {
            case Exit.End => _ok(Exit.End)
            case q => _no(q)
        }

        For(_2.shift(context)) { x =>
            _no.beforeExit {
                f(x)
            }
        } AndThen {
            case Exit.End => _ok(Exit.End)
            case q => _no(q)
        }
    }
}
