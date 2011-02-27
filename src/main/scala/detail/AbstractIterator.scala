

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


/**
 * Iterator implementation helper
 */
private[hano]
trait AbstractIterator[+A] {

    /**
     * Is iterator pass-the-end?
     */
    def isEnd: Boolean

    /**
     * Returns the current element.
     */
    def deref: A

    /**
     * Traverses to the next position.
     */
    def increment(): Unit

    private[this] var err: Throwable = null

    private def hasNext: Boolean = {
        delayThrow()
        !isEnd
    }

    private def next: A = {
        delayThrow()
        val tmp = deref
        try {
            increment()
        } catch {
            case t: Throwable => err = t
        }
        tmp
    }

    private def delayThrow() {
        if (err != null) {
            throw err
        }
    }

    final def concrete: Iterator[A] = new AbstractIterator.Concrete(this)
}


private[hano]
object AbstractIterator {
    private class Concrete[A](_1: AbstractIterator[A]) extends Iterator[A] {
        override def hasNext = _1.hasNext
        override def next = _1.next
    }
}
