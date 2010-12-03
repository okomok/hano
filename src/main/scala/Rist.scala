

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ArrayList
import scala.collection.JavaConversions._


/**
 * Infinite Seq list (immutable)
 */
final class Rist[A] extends Seq[A] {
    private[this] val xs = new ArrayList[A]
    private[this] val outs = new ArrayList[A => Unit]

    override def forloop(f: A => Unit, k: Exit => Unit) {
         for (x <- xs) {
            f(x)
        }
        outs.add(f)
    }
//    override def head: A = xs.get(0)

    def add(y: A) {
        xs.add(y)
        for (out <- outs) {
            out(y)
        }
    }
    def addAll(ys: scala.collection.TraversableOnce[A]) {
        for (y <- ys) {
            add(y)
        }
    }
}


object Rist {
    /**
     * Constructs a Rist with initial values.
     */
    def apply[A](inits: A*): Rist[A] = {
        val that = new Rist[A]
        that.addAll(inits)
        that
    }
}