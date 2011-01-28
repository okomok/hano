

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
class LoopSelf[A](_1: Seq[A]) extends Seq[A] {
    assert(_1.context eq Self)

    @volatile private[this] var isActive = true
    override def close() { isActive = false; _1.close() }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        isActive = true
        val _k = ExitOnce { q => f.exit(q); close() }

        while (isActive) {
            _1 onEach { x =>
                _k.beforeExit {
                    f(x)
                }
            } onExit {
                case Exit.End => ()
                case q => _k(q)
            } start()
        }

        if (!isActive) {
            _k(Exit.Closed)
        }
    }
}


private[hano]
class LoopOther[A](_1: Seq[A], _2: Int) extends Seq[A] {
    assert(_1.context ne Self)

    @volatile private[this] var isActive = true
    override def close() { isActive = false; _1.close() }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        isActive = true
        val _k = ExitOnce { q => f.exit(q); close() }

        def rec() {
            _1 onEach { x =>
                _k.beforeExit {
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
                        context.eval { // avoids to interleave the current context.
                            rec()
                        }
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
