

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private
class FromIterator[A](_1: Iterator[A]) extends Seq[A] {
    override def category = Category(Category.Iterable)
    override def begin = _1
}
