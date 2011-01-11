

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop[A](_1: Seq[A], _2: Int = 1) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Context.self) {
            new LoopSelf(_1)
        } else {
            new LoopOther(_1, _2)
        }
    }
}


private[hano]
class LoopSelf[A](_1: Seq[A]) extends Resource[A] {
    assert(_1.context eq Context.self)
    @volatile private[this] var isActive = true
    override def context = _1.context
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q) }

        _1 `for` { x =>
            _k.beforeExit {
                while (isActive) {
                    f(x)
                }
                _k(Exit.Closed)
            }
        } exit {
            case q @ Exit.Failed(_) => _k(q)
            case _ => ()
        }
    }
}


private[hano]
class LoopOther[A](_1: Seq[A], _2: Int) extends Resource[A] {
    assert(_1.context ne Context.self)
    @volatile private[this] var isActive = true
    override def context = _1.context
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q) }

        def rec() {
            _1 `for` { x =>
                _k.beforeExit {
                    for (i <- 0 until _2 if isActive) {
                        f(x)
                    }
                    if (isActive) {
                        rec()
                    } else {
                        _k(Exit.Closed)
                    }
                }
            } exit {
                case q @ Exit.Failed(_) => _k(q)
                case _ => ()
            }
        }
        rec()
    }
}
