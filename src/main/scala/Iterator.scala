

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Yet another Iterator: backend of <code>Iterative</code>.
 * Unlike <code>Iterator</code>, this separates element-access and traversing method.
 * (E.g. <code>.map(f).length</code> is inefficient in scala.Iterator abstraction.)
 */
@Annotation.notThreadSafe
trait Iterator[+A] {

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

    /**
     * Advances <code>n</code>.
     */
    final def advance(n: Int) {
        var it = n
        while (it != 0 && !isEnd) {
            increment()
            it -= 1
        }
    }

    /**
     * Advances while satisfying the predicate.
     */
    final def advanceWhile(p: A => Boolean) {
        while (!isEnd && p(deref)) {
            increment()
        }
    }
}
