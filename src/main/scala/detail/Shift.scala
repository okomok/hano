

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class ShiftToAsync[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    assert(_2.context ne Context.self)
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        val _k = ExitOnce { q => f.exit(q);close() }

        For(_1) { x =>
            For(context) { _ =>
                _k.beforeExit {
                    f(x)
                }
            } AndThen {
                case q @ Exit.Failed(_) => _k(q)
                case _ =>
            }
        } AndThen { q =>
            For(context) { _ =>
                _k(q)
            } AndThen {
                case Exit.Failed(t) => LogErr(t, "Reaction.exit error")
                case _ =>
            }
        }
    }
}


private[hano]
class ShiftToSelf[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = Context.self
    override def forloop(f: Reaction[A]) {
        val cur = Actor.self
        val _k = ExitOnce { q => cur ! q;f.exit(q);close() }

        For(_1) { x =>
            cur ! AsyncTask { () =>
                For(context) { _ =>
                    _k.beforeExit {
                        f(x)
                    }
                } AndThen {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ =>
                }
            }
        } AndThen { q =>
            cur ! AsyncTask { () =>
                For(context) { _ =>
                    _k(q)
                } AndThen {
                    case Exit.Failed(t) => LogErr(t, "Reaction.exit error")
                    case _ =>
                }
            }
        }

        var go = true
        while (go) {
            Actor.receive {
                case AsyncTask(f) => f()
                case _: Exit => go = false
            }
        }
    }
}
