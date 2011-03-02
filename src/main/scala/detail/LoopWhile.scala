

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class LoopWhile[A](_1: Seq[A], _2: () => Boolean) extends SeqProxy[A] {
    override val self = new LoopWhileOther(_1, _2)
/*
    override val self = {
        if (_1.context eq Self) {
            new LoopWhileSelf(_1, _2)
        } else {
            new LoopWhileOther(_1, _2)
        }
    }
*/
}


private[hano]
class LoopWhileOther[A](_1: Seq[A], _2: () => Boolean, grainSize: Int = 1) extends SeqAdapter.Of[A](_1) {
    //assert(_1.context ne Self)

    override def forloop(f: Reaction[A]) {
        //@volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        def rec() {
            _1 onEnter { p =>
                f.enter {
                    Exit { q =>
                        p(q)
                        isActive = false
                    }
                }
                if (!_2()) {
                    f.exit(Exit.Success)
                }
            } onEach { x =>
                f.beforeExit {
                    var i = 0
                    while (isActive && (i != grainSize)) {
                        i += 1
                        f(x)
                    }
                    if (!isActive) {
                        f.exit(Exit.Failure(break.Control))
                    }
                }
            } onExit { q =>
                f.beforeExit {
                    q match {
                        case Exit.Success => {
                            if (isActive) {
                                rec()
                            } else {
                                assert(f.isExited)
                            }
                        }
                        case q => f.exit(q)
                    }
                }
            } start()
        }

        rec()
    }
}

/*
// Specialized to avoid stack-overflow.
private[hano]
class LoopWhileSelf[A](_1: Seq[A], _2: () => Boolean) extends Seq[A] {
    assert(_1.context eq Self)

    @volatile private[this] var isActive = false
    override val context = _1.context.toKnown
    override def close() { isActive = false; _1.close() }

    // requires synchronized in case close-then-forloop from other threads.
    override def forloop(f: Reaction[A]) = synchronized {
        assert(!isActive)

        isActive = true
        def _k(q: Exit.Status) { close(); f.exit(q) }

        @scala.annotation.tailrec
        def rec() {
            if (!_2()) {
                context eval {
                    _k(Exit.Success)
                }
                return
            }

            _1.noSuccess onEach {
                f(_)
            } onExit {
                _k(_)
            } start()

            if (isActive) {
                rec()
            } else {
                _k(Exit.Closed)
            }
        }

        rec()
    }
}
*/
