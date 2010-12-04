

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, reset, shift}


object Block {

    private[hano] val Env = new Env
    sealed class Env {
        def amb[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.toCps
        def each[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.toCps

        def head[A](xs: Seq[A]): A @cpsParam[Any, Unit] = xs.take(1).toCps

        def nth[A](xs: Seq[A])(n: Int): A @cpsParam[Any, Unit] = xs.drop(n).take(1).toCps

        def find[A](xs: Seq[A])(p: A => Boolean): A @cpsParam[Any, Unit] = xs.dropWhile(!p(_)).take(1).toCps

        def apply[A](xs: Seq[A]): Forloop[A] = new Forloop(xs)

        sealed class Forloop[A](xs: Seq[A]) {
            def foreach(g: A => Any @cpsParam[Unit, Unit]): Exit @cpsParam[Any, Unit] = new Seq[Exit] {
                override def forloop(cp: Exit => Unit, k: Exit => Unit) {
                    xs.onExit(q => cp(q)).forloop(x => reset{g(x);()}, k)
                }
            } toCps
        }

        def require(cond: => Boolean): Unit @cpsParam[Any, Unit] =  (if (cond) Seq.single(()) else Seq.empty).toCps
    }

}
