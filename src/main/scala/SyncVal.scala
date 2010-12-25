

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


final class SyncVal[A] extends Reaction[A] with Reaction.Checked[A] {
    private[this] var v: Either[Throwable, A] = null
    private[this] val c = new java.util.concurrent.CountDownLatch(1)

    override protected def applyChecked(x: A) {
        if (v != null) {
            return
        }
        try {
            v = Right(x)
        } finally {
            c.countDown
        }
    }
    override protected def exitChecked(q: Exit) {
        try {
            q match {
                case Exit.Failed(t) => if (v == null) { v = Left(t) }
                case _ => ()
            }
        } finally {
            c.countDown
        }
    }

    def apply(): A = {
        c.await()
        if (v == null) {
            throw new NoSuchElementException("SyncVal.apply()")
        }
        v match {
            case Right(x) => x
            case Left(t) => throw t
        }
    }
}
