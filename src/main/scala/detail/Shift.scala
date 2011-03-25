

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends SeqProxy[A] {
    Require.notUnknown(_2.process, "shift")

    override val self = {
        val from = _1.process
        val to = _2.process
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
class ShiftToOther[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    assert(_2.process ne Self)
    override def process = _2.process

    override def forloop(f: Reaction[A]) {
        _1.onEnter { p =>
            process.invoke {
                f.enter(p)
            }
        } onEach { x =>
            f.beforeExit {
                process.invoke {
                    f(x)
                }
            }
        } onExit { q =>
            f.beforeExit {
                process.invoke {
                    f.exit(q)
                }
            }
        } start()
    }
}


private[hano]
class ShiftToSelf[A](_1: Seq[A]) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        val cur = Actor.self

        _1.onEnter { p =>
            cur ! Action {
                f.enter {
                    Exit { _ =>
                        cur ! Close
                    }
                }
                f.enter(p)
            }
        } onEach { x =>
            f.beforeExit {
                cur ! Action {
                    f(x)
                }
            }
        } onExit { q =>
            f.beforeExit {
                cur ! Action {
                    f.exit(q)
                }
            }
        } start()

        var go = true
        while (go) {
            Actor.receive {
                case Action(f) => f()
                case Close => go = false
            }
        }
    }
}
