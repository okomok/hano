

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.annotation.unchecked.uncheckedVariance


trait Seq[+A] extends Equals with PartialFunction[Int, A] { self =>


    def category: Category


// Reactive

    def forloop(f: A => Unit, q: Exit => Unit) {
        if (category <:< Category.RandomAccess) {
            Pre.range(start, end, "forloop")
            Exit.tryCatch(q) {
                var i = start; val j = end
                while (i != j) {
                    f(deref(i))
                    i += 1
                }
            }
            q(Exit.End)
        } else if (category <:< Category.Iterable) {
            Exit.tryCatch(q) {
                val i = begin
                while (!i.isEnd) {
                    f(i.deref)
                    i.increment
                }
            }
            q(Exit.End)
        } else {
            throw new Category.RequiresReactiveException("forloop")
        }
    }

    final def foreach(f: A => Unit) = forloop(f, _ => ())


// Iterable
    def begin: Iterator[A] = {
        if (category <:< Category.RandomAccess) {
            //new Iterator.RandomAccess(this)
            throw new Error
        } else {
            throw new Category.RequiresIterableException("begin")
        }
    }

    def equalsIf[B](that: Seq[B])(p: (A, B) => Boolean): Boolean = {
        if (category <:< Category.RandomAccess) {
            if (size != that.size) {
                false
            } else {
                throw new Error
                //stl.EqualIf(this, start, end, that, that.start, p)
            }
        } else if (category <:< Category.Iterable) {
            val i = begin; val j = that.begin
            while (!i.isEnd && !j.isEnd) {
                if (!p(i.deref, j.deref)) {
                    return false
                }
                i.increment(); j.increment()
            }
            i.isEnd && j.isEnd
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }

    def isEmpty: Boolean = {
        if (category <:< Category.RandomAccess) {
            start == end
        } else if (category <:< Category.Iterable) {
            begin.isEnd
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }

    def length: Int = {
        if (category <:< Category.RandomAccess) {
            end - start
        } else if (category <:< Category.Iterable) {
            var c = 0
            val i = begin
            while (!i.isEnd) {
                c += 1
                i.increment
            }
            c
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }


// RandomAccess

    def start: Int = throw new Category.RequiresRandomAccessException("start")
    def end: Int = throw new Category.RequiresRandomAccessException("end")

    def read(i: Int): A = throw new Category.RequiresRandomAccessException("read")
    def write(i: Int, x: A @uncheckedVariance): Unit = throw new Category.RequiresRandomAccessException("write")

    override def isDefinedAt(i: Int) = throw new UnsupportedOperationException("isDefinedAt")
    override def apply(i: Int): A = throw new UnsupportedOperationException("apply")

    object deref extends (Int => A) {
        def apply(i: Int): A = self.read(i)
        def update(i: Int, x: A @uncheckedVariance) = {
            assert(self.category <:< Category.Writable)
            self.write(i, x)
        }
    }

    def size: Int = {
        if (category <:< Category.RandomAccess) {
            end - start
        } else {
            throw new Category.RequiresRandomAccessException("size")
        }
    }

    def par: Seq[A] = this

}
