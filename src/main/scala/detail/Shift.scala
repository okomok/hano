

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends SeqProxy[A] {
    require(_2.context ne Unknown)

    override val self = {
        val from = _1.context
        val to = _2.context
        if (from eq to) {
            _1
        } else if (to eq Self) {
            new ShiftToSelf(_1)
        } else {
            new ShiftToOther(_1, _2)
        }
    }
}


private[hano]
class ShiftToSelf[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = Self
    override def forloop(f: Reaction[A]) {
        val cur = Actor.self
        val _k = ExitOnce { q => cur ! q; f.exit(q); close() }

        _1 onEach { x =>
            cur ! Action {
                context onEach { _ =>
                    _k.beforeExit {
                        f(x)
                    }
                } onExit {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ => ()
                } start()
            }
        } onExit { q =>
            cur ! Action {
                context onEach { _ =>
                    _k(q)
                } onExit {
                    case Exit.Failed(t) => LogErr(t, "Reaction.exit error")
                    case _ => ()
                } start()
            }
        } start()

        var go = true
        while (go) {
            Actor.receive {
                case Action(f) => f()
                case _: Exit => go = false
            }
        }
    }
}


private[hano]
class ShiftToOther[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    assert(_2.context ne Self)
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q); close() }

        _1 onEach { x =>
            context onEach { _ =>
                _k.beforeExit {
                    f(x)
                }
            } onExit {
                case q @ Exit.Failed(_) => _k(q)
                case _ => ()
            } start()
        } onExit { q =>
            context onEach { _ =>
                _k(q)
            } onExit {
                case Exit.Failed(t) => LogErr(t, "Reaction.exit error")
                case _ => ()
            } start()
        } start()
    }
}
