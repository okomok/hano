

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.nio.channels.{Selector, SelectionKey, ClosedSelectorException}
import scala.collection.JavaConversions._


object Nio {

    def selection(s: Selector): Seq[SelectionKey] = Selection1(s)
    def selection(s: Selector, t: Long): Seq[SelectionKey] = Selection2(s, t)

    case class Selection1(_1: Selector) extends Forwarder[SelectionKey] {
        override protected val delegate: Seq[SelectionKey] = new _Selection(_1, _.select)
    }

    case class Selection2(_1: Selector, _2: Long) extends Forwarder[SelectionKey] {
        override protected val delegate: Seq[SelectionKey] = new _Selection(_1, _.select(_2))
    }

    private class _Selection(_1: Selector, _2: Selector => Long) extends Seq[SelectionKey] {
        override def forloop(f: SelectionKey => Unit, k: Exit => Unit) {
            Exit.tryCatch(k) {
                try {
                    while (true) {
                        if (_2(_1) != 0) {
                            val keys = _1.selectedKeys
                            for (key <- keys) {
                                f(key.asInstanceOf[SelectionKey])
                            }
                            keys.clear()
                        }
                    }
                } catch  {
                    case _: ClosedSelectorException => // k(End)? k(Closed)?
                }
            }
        }
    }

}
