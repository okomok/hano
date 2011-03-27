

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.nio.channels.{Selector, SelectionKey, ClosedSelectorException}


object Nio {

    /**
     * Selects a set of keys whose corresponding channels are ready for I/O operations.
     */
    def select(s: Selector): Seq[SelectionKey] = new Select(s, _.select())
    def select(s: Selector, _timeout: Long): Seq[SelectionKey] = new Select(s, _.select(_timeout))
    def selectNow(s: Selector): Seq[SelectionKey] = new Select(s, _.selectNow())


    private class Select(_1: Selector, _2: Selector => Int) extends Seq[SelectionKey] {
        override def process = Self

        override def forloop(f: Reaction[SelectionKey]) {
            val loop = new detail.Loop

            f.enter {
                loop.exit
            } applying {
                try {
                    while (!loop.breaks) {
                        if (_2(_1) != 0) {
                            val keys = _1.selectedKeys
                            for (key <- Iter.from(keys).able) {
                                f(key.asInstanceOf[SelectionKey])
                            }
                            keys.clear()
                        }
                    }
                } catch {
                    case t: ClosedSelectorException => f.fail(t)
                }
            } exit {
                loop.status
            }
        }
    }
}
