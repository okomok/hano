

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano; package iterable


private[hano]
class Append[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def category = Category(Category.Iterable)
    override def begin = new Iterator[A] {
        private[this] var it = _1.begin
        private[this] var inLeft = true
        ready()

        override def isEnd = it.isEnd
        override def deref = it.deref
        override def increment() {
            it.increment
            ready()
        }

        private def ready() {
            if (it.isEnd && inLeft) {
                it = _2.begin
                inLeft = false
            }
        }
    }
}
