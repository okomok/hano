

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


private[hano]
class LoopSelf[A](_1: Seq[A]) extends Resource[A] {
    assert(_1.context eq Self)
    @volatile private[this] var isActive = true
    override def context = _1.context
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q) }

        _1 onEach { x =>
            _k.beforeExit {
                while (isActive) {
                    f(x)
                }
                _k(Exit.Closed)
            }
        } onExit {
            case q @ Exit.Failed(_) => _k(q)
            case _ => ()
        } start()
    }
}


private[hano]
class LoopOther[A](_1: Seq[A], _2: Int) extends Resource[A] {
    assert(_1.context ne Self)
    @volatile private[this] var isActive = true
    override def context = _1.context
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q) }

        def rec() {
            _1 onEach { x =>
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
            } onExit {
                case q @ Exit.Failed(_) => _k(q)
                case _ => ()
            } start()
        }
        rec()
    }
}
