

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.LinkedList


private[hano]
class Zip[A, B](_1: Seq[A], _2: Seq[B]) extends Seq[(A, B)] {
    override def close() = { _1.close(); _2.close() }
    override val context = _1.context upper _2.context
    override def forloop(f: Reaction[(A, B)]) {
        var ends1 = false
        var ends2 = false
        val c1 = new LinkedList[A]
        val c2 = new LinkedList[B]
        val _k = ExitOnce { q => close(); f.exit(q) }
        def invariant = assert(c1.isEmpty || c2.isEmpty)

        _1 shift {
            context
        } onEach { x =>
            _k beforeExit {
                invariant
                if (c2.isEmpty) {
                    c1.add(x)
                } else {
                    f(x, c2.poll)
                }
            }
        } onExit {
            case Exit.End => {
                ends1 = true
                if (ends2 || c1.isEmpty) {
                    _k(Exit.End)
                }
            }
            case q => _k(q) // fail-immediately
        } start()

        _2 shift {
            context
        } onEach { y =>
            _k beforeExit {
                invariant
                if (c1.isEmpty) {
                    c2.add(y)
                } else {
                    f(c1.poll, y)
                }
            }
        } onExit {
            case Exit.End => {
                invariant
                ends2 = true
                if (ends1 || c2.isEmpty) {
                    _k(Exit.End)
                }
            }
            case q => _k(q)
        } start()
    }
}
