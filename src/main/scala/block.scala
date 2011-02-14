

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, reset, shift}


object block {

    def apply[A](ctx: Env => A @cpsParam[A, Any]): Unit = reset(ctx(Env))

    // Single-element sequence is usable also in generator.cps.
    private[hano] class Env1 {
        def isEmpty[A](xs: Seq[A]): Boolean @cpsParam[Any, Unit] = xs.isEmpty.toCps
        def length[A](xs: Seq[A]): Int @cpsParam[Any, Unit] = xs.length.toCps
        def size[A](xs: Seq[A]): Int @cpsParam[Any, Unit] = xs.size.toCps
        def head[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.head.toCps
        def last[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.last.toCps
        def nth[A](xs: Seq[A])(n: Int): A @cpsParam[Any, Unit] = xs.nth(n).toCps
        def find[A](xs: Seq[A])(p: A => Boolean): A @cpsParam[Any, Unit] = xs.dropWhile(!p(_)).take(1).toCps
        def foldLeft[A, B](xs: Seq[A])(z: B)(op: (B, A) => B): B @cpsParam[Any, Unit] = xs.foldLeft(z)(op).toCps
        def reduceLeft[A](xs: Seq[A])(op: (A, A) => A): A @cpsParam[Any, Unit] = xs.reduceLeft(op).toCps
        def require(cond: Boolean): Unit @cpsParam[Any, Unit] =  (if (cond) Single(()) else Empty).toCps
        def use[A](xs: Arm[A]): A @cpsParam[Any, Unit] = xs.toCps
    }

    private val Env = new Env
    sealed class Env extends Env1 {
        def each[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.toCps

        def in[A](xs: Seq[A]): In[A] = new In(xs)

        sealed class In[A](xs: Seq[A]) {
            def foreach(g: A => Any @cpsParam[Unit, Unit]): Exit @cpsParam[Any, Unit] = new Seq[Exit] {
                override def context = xs.context
                override def forloop(cp: Reaction[Exit]) {
                    xs.onExit(q => cp(q)).forloop(Reaction(x => reset{g(x);()}, q => cp.exit(q)))
                }
            } toCps
        }
    }

    private[hano] def discardValue[A](v: => A @cpsParam[Unit, Unit]): A @cpsParam[Any, Unit] = {
        shift { k: (A => Any) =>
            reset {
                k(v)
                ()
            }
        }
    }

}
