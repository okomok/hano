

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        val _k = CallOnce[Exit] { q => f.exit(q);close() }

        For(_1) { x =>
            For(context) { _ =>
                if (!_k.isDone) {
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
class ShiftToAsync[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    assert(_2.context ne Context.self)
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        val _k = CallOnce[Exit] { q => f.exit(q);close() }

        For(_1) { x =>
            For(context) { _ =>
                if (!_k.isDone) {
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


import scala.actors.Actor

private[hano]
class ShiftToSelf[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = Context.self
    override def forloop(f: Reaction[A]) {
        val _k = CallOnce[Exit] { q => f.exit(q);close() }
        val cur = Actor.self

        For(_1) { x =>
            cur ! Context.newTask {
                For(context) { _ =>
                    if (!_k.isDone) {
                        f(x)
                    }
                } AndThen {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ =>
                }
            }
        } AndThen { q =>
            cur ! Context.newTask{_k(q)}
        }

        Actor.loop {
            Actor.react {
                case Context.Task(f) => f()
                case Exit => Actor.exit
            }
        }
    }
}
