

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.nio.channels.{Selector, SelectionKey, ClosedSelectorException}


object Nio {
    def selection(s: Selector): Seq[SelectionKey] = Selection1(s)
    def selection(s: Selector, t: Long): Seq[SelectionKey] = Selection2(s, t)

    case class Selection1(_1: Selector) extends SeqProxy[SelectionKey] {
        override val self = new _Selection(_1, _.select).asSeq
    }

    case class Selection2(_1: Selector, _2: Long) extends SeqProxy[SelectionKey] {
        override val self = new _Selection(_1, _.select(_2)).asSeq
    }

    private class _Selection(_1: Selector, _2: Selector => Long) extends Seq[SelectionKey] {
        override def process = Self
        override def forloop(f: Reaction[SelectionKey]) {
            f.enter {
                Exit.Empty
            } applying {
                try {
                    while (true) {
                        if (_2(_1) != 0) {
                            val keys = _1.selectedKeys
                            for (key <- Iter.from(keys).able) {
                                f(key.asInstanceOf[SelectionKey])
                            }
                            keys.clear()
                        }
                    }
                } catch  {
                    case t: ClosedSelectorException => f.exit(Exit.Failure(t))
                }
            } exit {
                Exit.Success
            }
        }
    }
}
