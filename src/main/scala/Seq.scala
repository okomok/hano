

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait Seq[+A] extends Equals with PartialFunction[Int, A] { self =>


    def category: Category


// Reactive

    def forloop(f: A => Unit, q: Exit => Unit) {
        if (traversal <:< Category.RandomAccess) {
            Pre.validRange(start, end)
            val i = start
            val j = end
            while (i != j) {
                f(deref(i))
                i += 1
            }
            q(Exit.End)
        } else if (traversal <:< Category.Iterable) {
            val it = begin
            while (!it) {
                f(~it)
                it.++
            }
            q(Exit.End)
        } else {
            throw new Category.RequiresReactiveException("Seq.forloop")
        }
    }


// Iterable

    def begin: Iterator = {
        if (traversal <:< Category.RandomAccess) {
            new Iterator.RandomAccess(this)
        } else {
            throw new Category.RequiresIterableException("Seq.begin")
        }
    }


// RandomAccess

    def start: Int = throw new Category.RequiresRandomAccessException("Seq.start")
    def end: Int = throw new Category.RequiresRandomAccessException("Seq.end")

    def read(i: Int): A = throw new Category.RequiresRandomAccessException("Seq.read")
    def write(i: Int, x: A @scala.annotation.uncheckedVariance): Unit = throw new Category.RequiresRandomAccessException("Seq.write")

    override def isDefinedAt(i: Int) = throw new UnsupportedOperationException("Seq.isDefinedAt")
    override def apply(i: Int): throw new UnsupportedOperationException("Seq.apply")

    object deref extends (Int => A) {
        def apply(i: Int): A = self.read(i)
        def update(i: Int, x: A) = {
            assert(self.Category <:< Category.Writable)
            self.write(i, x)
        }
    }

    def par: Seq[A] = this

}
