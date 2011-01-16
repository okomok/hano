

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


/**
 * Iterator implementation helper
 */
private[hano]
trait AbstractIterator[+A] extends Iterator[A] {

    /**
     * Is iterator pass-the-end?
     */
    protected def isEnd: Boolean

    /**
     * Returns the current element.
     */
    protected def deref: A

    /**
     * Traverses to the next position.
     */
    protected def increment(): Unit

    private[this] var err: Throwable = null

    final override def hasNext: Boolean = {
        delayThrow()
        !isEnd
    }

    final override def next: A = {
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
}
