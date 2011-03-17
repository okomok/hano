

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnReact[A](_1: Seq[A],
    _2: Exit => Unit, _3: A => Unit, _4: Exit.Status => Unit) extends SeqAdapter.Of[A](_1)
{
    override def forloop(f: Reaction[A]) {
        _1.forloop(new OnReact.ReactionImpl(f, _2, _3, _4))
    }
}

private[hano]
object OnReact {
    private class ReactionImpl[A](_1: Reaction[A],
        _2: Exit => Unit, _3: A => Unit, _4: Exit.Status => Unit) extends Reaction[A]
    {
        override protected def rawEnter(p: Exit) {
            _2(p)
            _1.enter(p)
        }
        override protected def rawApply(x: A) {
            _3(x)
            _1(x)
        }
        override protected def rawExit(q: Exit.Status) {
            _4(q)
            _1.enter() // Force _1 to enter, for _2(p) may exit.
            _1.exit(q)
        }
    }
}


private[hano]
class OnEnter[A](_1: Seq[A], _2: Exit => Unit) extends SeqProxy[A] {
    override val self = new OnReact[A](_1, _2, _ => (), _ => ()).asSeq
}


private[hano]
class OnEach[A](_1: Seq[A], _2: A => Unit) extends SeqProxy[A] {
    override val self = new OnReact[A](_1, _ => (), _2, _ => ()).asSeq
}

private[hano]
class OnEachMatch[A](_1: Seq[A], _2: PartialFunction[A, Unit]) extends SeqProxy[A] {
    override val self = new OnReact[A](_1, _ => (), x => if (_2.isDefinedAt(x)) _2(x), _ => ()).asSeq
}


private[hano]
class OnExit[A](_1: Seq[A], _2: Exit.Status => Unit) extends SeqProxy[A] {
    override val self = new OnReact[A](_1, _ => (), _ => (), _2).asSeq
}

private[hano]
class OnSuccess[A](_1: Seq[A], _2: () => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Success => _2()
        case _ => ()
    }
}

private[hano]
class OnFailure[A](_1: Seq[A], _2: Throwable => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Failure(t) => _2(t)
        case _ => ()
    }
}
