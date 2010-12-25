

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.LinkedList


private[hano]
class Zip[A, B](_1: Seq[A], _2: Seq[B]) extends Seq[(A, B)] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: Reaction[(A, B)]) {
        var ends1 = false
        var ends2 = false
        val c1 = new LinkedList[A]
        val c2 = new LinkedList[B]
        val lock = new AnyRef{}
        val _k = CallOnce[Exit] { q => f.onExit(q);close() }
        def invariant = assert(c1.isEmpty || c2.isEmpty)

        LockedFor(_1, lock) { x =>
            if (!_k.isDone) {
                invariant
                if (c2.isEmpty) {
                    c1.add(x)
                } else {
                    f(x, c2.poll)
                }
            }
        } AndThen {
            case Exit.End => {
                ends1 = true
                if (ends2 || c1.isEmpty) {
                    _k(Exit.End)
                }
            }
            case q => _k(q) // fail-immediately
         }

        LockedFor(_2, lock) { y =>
            if (!_k.isDone) {
                invariant
                if (c1.isEmpty) {
                    c2.add(y)
                } else {
                    f(c1.poll, y)
                }
            }
        } AndThen {
            case Exit.End => {
                invariant
                ends2 = true
                if (ends1 || c2.isEmpty) {
                    _k(Exit.End)
                }
            }
            case q => _k(q)
        }
    }
}
