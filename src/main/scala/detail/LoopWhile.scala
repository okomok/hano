

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class LoopWhile[A](_1: Seq[A], _2: () => Boolean) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Self) {
            new LoopWhileIfSelf(_1, _2, _.isSuccess)
        } else {
            new LoopWhileIfOther(_1, _2, _.isSuccess)
        }
    }
}


private[hano]
class LoopWhileIfOther[A](_1: Seq[A], _2: () => Boolean, cond: Exit.Status => Boolean) extends SeqAdapter.Of[A](_1) {
    assert(_1.context ne Self)

    override def forloop(f: Reaction[A]) {
        @volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        def rec() {
            _1.onEnter { p =>
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
                    f(x)
                    if (!isActive) {
                        f.exit(status) // exit immediately
                    }
                }
            } onExit { q =>
                f.beforeExit {
                    if (cond(q)) {
                        if (isActive) {
                            rec()
                        } else {
                            f.exit(status)
                        }
                    } else {
                        f.exit(q)
                    }
                }
            } start()
        }

        rec()
    }
}


// Specialized to avoid stack-overflow.
private[hano]
class LoopWhileIfSelf[A](_1: Seq[A], _2: () => Boolean, cond: Exit.Status => Boolean) extends SeqAdapter.Of[A](_1) {
    assert(_1.context eq Self)

    override def forloop(f: Reaction[A]) {
        @volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        var go = true
        while (go) {
            go = false
            _1.onEnter { p =>
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
                    f(x)
                    if (!isActive) {
                        f.exit(status) // exit immediately
                    }
                }
            } onExit { q =>
                f.beforeExit {
                    if (cond(q)) {
                        if (isActive) {
                            go = true
                        } else {
                            f.exit(status)
                        }
                    } else {
                        f.exit(q)
                    }
                }
            } start()
        }
    }
}
