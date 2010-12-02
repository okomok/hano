

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class FromArray[A](_1: Array[A]) extends Forwarder[A] {
    override protected val delegate = Seq.from(_1)
}


private class FromTraversable[A](_1: scala.collection.Traversable[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.foreach(f)
        }
        k(End)
    }
}


private class FromOption[A](_1: Option[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            if (!_1.isEmpty) {
                f(_1.get)
            }
        }
        k(End)
    }
}


private class FromResponder[A](_1: Responder[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.respond(f)
        }
        k(End)
    }
}

private class ToResponder[A](_1: Seq[A]) extends Responder[A] {
    override def respond(f: A => Unit) = _1.foreach(f)
}
