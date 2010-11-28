

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.annotation.unchecked.uncheckedVariance


object Seq {

    @Annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    implicit def fromIterator[A](from: Iterator[A]): Seq[A] = new FromIterator(from)

}


trait Seq[+A] extends /*Equals with*/ PartialFunction[Int, A] { self =>


    def category: Category

    final def isReactive: Boolean = true
    final def isIterable: Boolean = category <:< Category.Iterable
    final def isRandomAccess: Boolean = category <:< Category.RandomAccess
    final def isStrict: Boolean = category <:< Category.Strict
    final def isWritable: Boolean = category <:< Category.Writable


// Reactive

    def forloop(f: A => Unit, q: Exit => Unit) {
        if (isRandomAccess) {
            Pre.range(start, end, "forloop")
            Exit.tryCatch(q) {
                var it = start; val jt = end
                while (it != jt) {
                    f(deref(it))
                    it += 1
                }
            }
            q(Exit.End)
        } else if (isIterable) {
            Exit.tryCatch(q) {
                val it = begin
                while (!it.isEnd) {
                    f(it.deref)
                    it.increment
                }
            }
            q(Exit.End)
        } else {
            throw new Category.RequiresReactiveException("forloop")
        }
    }

    def foreach(f: A => Unit) = forloop(f, _ => ())


// Iterable

    def begin: Iterator[A] = {
        if (isRandomAccess) {
            //new Iterator.RandomAccess(this)
            throw new Error
        } else {
            throw new Category.RequiresIterableException("begin")
        }
    }

    def equalsIf[B](that: Seq[B])(p: (A, B) => Boolean): Boolean = {
        if (isRandomAccess) {
            if (size != that.size) {
                false
            } else {
                throw new Error
                //stl.EqualIf(this, start, end, that, that.start, p)
            }
        } else if (isIterable) {
            val it = begin; val jt = that.begin
            while (!it.isEnd && !jt.isEnd) {
                if (!p(it.deref, jt.deref)) {
                    return false
                }
                it.increment(); jt.increment()
            }
            it.isEnd && jt.isEnd
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }

    def isEmpty: Boolean = {
        if (isRandomAccess) {
            start == end
        } else if (isIterable) {
            begin.isEnd
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }

    def length: Int = {
        if (isRandomAccess) {
            end - start
        } else if (isIterable) {
            var c = 0
            val it = begin
            while (!it.isEnd) {
                c += 1
                it.increment
            }
            c
        } else {
            throw new Category.RequiresIterableException("isEmpty")
        }
    }

    def append[B >: A](that: Seq[B]): Seq[B] = {
        val cat = category upper that.category
        if (cat <:< Category.RandomAccess) {
            new iterable.Append[B](this, that)
        } else if (cat <:< Category.Iterable) {
            new iterable.Append[B](this, that)
        } else {
            new iterable.Append[B](this, that)
        }
    }
    final def ++[B >: A](that: Seq[B]): Seq[B] = append(that)

    def forall(p: A => Boolean): Boolean = find(x => !p(x)).isEmpty

    def exists(p: A => Boolean): Boolean = !find(p).isEmpty

    def count(p: A => Boolean): Int = {
        if (isIterable) {
            var c = 0
            val it = begin
            while (!it.isEnd) {
                if (p(it.deref)) {
                    c += 1
                }
                it.increment()
            }
            c
        } else {
            throw new Category.RequiresIterableException("count")
        }
    }

    def find(p: A => Boolean): Option[A] = {
        if (isIterable) {
            val it = begin
            while (!it.isEnd) {
                val x = it.deref
                if (p(x)) {
                    return Some(x)
                }
                it.increment
            }
            None
        } else {
            throw new Category.RequiresIterableException("find")
        }
    }

    def foldLeft[B](z: B)(op: (B, A) => B): B = {
        if (isIterable) {
            var acc = z
            val it = begin
            while (!it.isEnd) {
                acc = op(acc, it.deref)
                it.increment
            }
            acc
        } else {
            throw new Category.RequiresIterableException("foldLeft")
        }
    }

    final def /:[B](z: B)(op: (B, A) => B): B = foldLeft(z)(op)

    def reduceLeft[B >: A](op: (B, A) => B): B = {
        if (isIterable) {
            val it = begin
            if (it.isEnd) {
                throw new UnsupportedOperationException("reduceLeft on empty sequence")
            }
            val x = it.deref
            it.increment
            Seq.from(it).foldLeft[B](x)(op)
        } else {
            throw new Category.RequiresIterableException("reduceLeft")
        }
    }

    def head: A = {
        if (isIterable) {
            val it = begin
            if (it.isEnd) {
                throw new NoSuchElementException("head on empty sequence")
            } else {
                it.deref
            }
        } else {
            throw new Category.RequiresIterableException("head")
        }
    }

    def last: A = {
        if (isIterable) {
            var x: Option[A] = None
            val it = begin
            while (!it.isEnd) {
                x = Some(it.deref)
                it.increment
            }
            x getOrElse {
                throw new NoSuchElementException("last on empty sequence")
            }
        } else {
            throw new Category.RequiresIterableException("last")
        }
    }


// RandomAccess

    def start: Int = throw new Category.RequiresRandomAccessException("start")
    def end: Int = throw new Category.RequiresRandomAccessException("end")

    def read(it: Int): A = throw new Category.RequiresRandomAccessException("read")
    def write(it: Int, x: A @uncheckedVariance): Unit = throw new Category.RequiresRandomAccessException("write")

    override def isDefinedAt(it: Int) = throw new UnsupportedOperationException("isDefinedAt")
    override def apply(it: Int): A = throw new UnsupportedOperationException("apply")

    object deref extends (Int => A) {
        def apply(it: Int): A = self.read(it)
        def update(it: Int, x: A @uncheckedVariance) = {
            assert(self.category <:< Category.Writable)
            self.write(it, x)
        }
    }

    def size: Int = {
        if (isRandomAccess) {
            end - start
        } else {
            throw new Category.RequiresRandomAccessException("size")
        }
    }

    def par: Seq[A] = this

}
