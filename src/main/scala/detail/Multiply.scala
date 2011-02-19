

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// No End sequence
private[hano]
class Multiply[A](_1: Seq[A], _2: Int) extends SeqResource[A] {
    private[this] var isActive = false
    override def context = _1.context
    override def closeResource() { isActive = false; _1.close() }
    override def openResource(f: Reaction[A]) {
        assert(!isActive)

        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        _1 onEach { x =>
            synchronized {
                f beforeExit {
                    var i = 0
                    while (isActive && (i != _2)) {
                        i += 1
                        f(x)
                    }
                    if (!isActive) {
                        _k(Exit.Closed)
                    }
                }
            }
        } onExit {
            case q @ Exit.End => ()
            case q => _k(q)
        } start()
    }
}
