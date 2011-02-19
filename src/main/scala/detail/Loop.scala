

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop[A](_1: Seq[A]) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Self) {
            new LoopSelf(_1)
        } else {
            new LoopOther(_1)
        }
    }
}


private[hano]
class LoopOther[A](_1: Seq[A]) extends SeqResource[A] {
    assert(_1.context ne Self)

    private[this] var isActive = false
    override def context = _1.context
    override def closeResource() { isActive = false; _1.close() }
    override def openResource(f: Reaction[A]) {
        require(!isActive)

        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        def rec() {
            _1 onEach { x =>
                synchronized { // is needed because...
                    f(x) // may reenter `forloop` from other threads.
                    if (!isActive) {
                        _k(Exit.Closed)
                        // `isActive` is out of play for me.
                    }
                }
                // Now you can reenter `forloop`.
            } onExit { q =>
                q match {
                    case Exit.End => {
                        if (isActive) {
                            rec()
                        }
                    }
                    case q => _k(q)
                }
            } start()
        }
        rec()
    }
}


// Specialized to avoid stack-overflow.
private[hano]
class LoopSelf[A](_1: Seq[A]) extends Seq[A] {
    assert(_1.context eq Self)

    @volatile private[this] var isActive = false
    override def context = _1.context
    override def close() { isActive = false; _1.close() }

    // requires synchronized in case close-then-forloop from other threads.
    override def forloop(f: Reaction[A]): Unit = synchronized {
        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        while (isActive) {
            _1.noEnd onEach {
                f(_)
            } onExit {
                _k(_)
            } start()
        }

        _k(Exit.Closed)
    }
}
