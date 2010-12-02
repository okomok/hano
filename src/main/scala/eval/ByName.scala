

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


import java.util.concurrent.Callable


class ByName[R](f: => R) extends Function0[R] {
    override def apply = f

    def _1: Function0[R] = () => f

    def toRunnable: Runnable = new Runnable {
        override def run = apply
    }

    def toCallable[S](implicit pre: R <:< S): Callable[S] = new Callable[S] {
        override def call: S = pre(apply)
    }
}

object ByName extends Strategy {
    def apply[R](f: => R) = new ByName(f)
    def unapply[R](from: ByName[R]): Option[Function0[R]] = Some(from._1)
    override def apply[R](f: ByName[R]) = new ByName(f())
    implicit def _fromExpr[R](from: => R): ByName[R] = new ByName(from)
    implicit def _toRunnable[R](from: ByName[R]): Runnable = from.toRunnable
    implicit def _toCallable[R](from: ByName[R]): Callable[R] = from.toCallable
}
