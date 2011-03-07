

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class LoopWhile[A](_1: Seq[A], _2: () => Boolean) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Self) {
            new LoopWhileSelf(_1, _2)
        } else {
            new LoopWhileOther(_1, _2)
        }
    }
}


private[hano]
class LoopWhileOther[A](_1: Seq[A], _2: () => Boolean, grainSize: Int = 1) extends SeqAdapter.Of[A](_1) {
    assert(_1.context ne Self)

    override def forloop(f: Reaction[A]) {
        @volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        def rec(xs: Seq[A]) {
            xs.onEnter { p =>
                f.enter {
                    Exit { q =>
                        p(q)
                        status = Exit.Failure(Exit.ByOther(q))
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
                        f.exit(status)
                    }
                }
            } onExit { q =>
                f.beforeExit {
                    q match {
                        case Exit.Success => {
                            if (isActive) {
                                rec(xs)
                            } else {
                                assert(f.isExited)
                            }
                        }
                        case q => f.exit(q)
                    }
                }
            } start()
        }

        rec(_1)
    }
}


// Specialized to avoid stack-overflow.
private[hano]
class LoopWhileSelf[A](_1: Seq[A], _2: () => Boolean) extends SeqAdapter.Of[A](_1) {
    assert(_1.context eq Self)

    override def forloop(f: Reaction[A]) {
        @volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        @scala.annotation.tailrec
        def rec() {
            _1.noSuccess.onEnter { p =>
                f.enter {
                    Exit { q =>
                        p(q)
                        status = Exit.Failure(Exit.ByOther(q))
                        isActive = false
                    }
                }
                if (!_2()) {
                    f.exit(Exit.Success)
                }
            } onEach {
                f(_)
            } onExit {
                f.exit(_)
            } start()

            if (isActive) {
                rec()
            } else {
                f.exit(status)
            }
        }

        rec()
    }
}
