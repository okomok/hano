

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop[A](_1: Seq[A], _2: Int = 1) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Self) {
            new LoopSelf(_1)
        } else {
            new LoopOther(_1, _2)
        }
    }
}


// Specialized to avoid stack-overflow.
private[hano]
class LoopSelf[A](_1: Seq[A]) extends SeqResource[A] {
    assert(_1.context eq Self)

    private[this] var isActive = true
    override def context = _1.context
    override def closeResource() { isActive = false; _1.close() }
    // requires synchronized in case close-then-forloop from other threads.
    override def openResource(f: Reaction[A]) {
        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        while (isActive) {
            _1.noEnd onEach {
                f(_)
            } onExit {
                _k(_)
            } start()
        }

        if (!isActive) {
            _k(Exit.Closed)
        }
    }
}


private[hano]
class LoopOther[A](_1: Seq[A], _2: Int) extends SeqResource[A] {
    assert(_1.context ne Self)

    private[this] var isActive = true
    override def context = _1.context
    override def closeResource() { isActive = false; _1.close() }
    override def openResource(f: Reaction[A]) {
        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        def rec() {
            _1 onEach { x =>
                f beforeExit {
                    for (i <- 0 until _2) {
                        if (isActive) {
                            f(x)
                        }
                    }
                    if (!isActive) {
                        _k(Exit.Closed)
                    }
                }
            } onExit {
                case Exit.End => {
                    if (isActive) {
                        rec()
                    } else {
                        _k(Exit.Closed)
                    }
                }
                case q @ Exit.Closed => {
                    if (isActive) {
                        _k(q)
                    }
                }
                case q => _k(q)
            } start()
        }
        rec()
    }
}
