

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Mail


object Mail {

    case class Enter(exit: hano.Exit) extends Mail

    case class Apply(element: Any) extends Mail

    case class Exit(status: hano.Exit.Status) extends Mail


    private[hano]
    class ToMails(_1: Seq[_]) extends SeqAdapter.Of[Mail](_1) {
        override def forloop(f: Reaction[Mail]) {
            _1.onEnter { q =>
                f.enter()
                f(Mail.Enter(q))
            } onEach { x =>
                f(Mail.Apply(x))
            } onExit { q =>
                f(Mail.Exit(q))
                f.exit()
            } start()
        }
    }

    private[hano]
    class FromMails(_1: Seq[Mail]) extends SeqAdapter.Of[Any](_1) {
        override def forloop(f: Reaction[Any]) {
            _1.onEnter {
                f.enter(_)
            } onEach {
                case Mail.Enter(p) => f.enter(p)
                case Mail.Apply(x) => f(x)
                case Mail.Exit(q) => f.exit(q)
            } onExit {
                f.exit(_)
            } start()
        }
    }
}
