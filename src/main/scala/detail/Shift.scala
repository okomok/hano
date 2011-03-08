

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends SeqProxy[A] {
    require(_2.process ne Unknown)

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
class ShiftToSelf[A](_1: Seq[A]) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        val cur = Actor.self
        def _exit(q: Exit.Status) { cur ! q; f.exit(q) }

        _1.onEnter { p =>
            cur ! Action {
                process.single.noSuccess.onEach { _ =>
                    f.enter(p)
                } onExit {
                    _exit
                } start()
            }
        } onEach { x =>
            f.beforeExit {
                cur ! Action {
                    process.single.noSuccess.onEach { _ =>
                        f(x)
                    } onExit {
                        _exit
                    } start()
                }
            }
        } onExit { q =>
            f.beforeExit {
                cur ! Action {
                    process.single.onEach { _ =>
                        _exit(q)
                    } onExit {
                        case Exit.Failure(t) => LogErr(t, "Reaction.exit error")
                        case _ => ()
                    } start()
                }
            }
        } start()

        var go = true
        while (go) {
            Actor.receive {
                case Action(f) => f()
                case _: Exit.Status => go = false
            }
        }
    }
}


private[hano]
class ShiftToOther[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    assert(_2.process ne Self)
    override def process = _2.process

    override def forloop(f: Reaction[A]) {
        _1.onEnter { p =>
            process.single.noSuccess.onEach { _ =>
                f.enter(p)
            } onExit {
                f.exit
            } start()

        } onEach { x =>
            f.beforeExit {
                process.single.noSuccess.onEach { _ =>
                    f(x)
                } onExit {
                    f.exit
                } start()
            }
        } onExit { q =>
            f.beforeExit {
                process.single.onEach { _ =>
                    f.exit(q)
                } onExit {
                    case Exit.Failure(t) => LogErr(t, "Reaction.exit error")
                    case _ => ()
                } start()
            }
        } start()
    }
}
