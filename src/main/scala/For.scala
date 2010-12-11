

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private case class For[A](xs: Seq[A]) {
    def apply(f: A => Unit) = new Apply(f)

    class Apply(f: A => Unit) {
        def AndThen(k: Exit => Unit){
            xs.forloop(f, k)
        }
    }
}

private case class LockedFor[A](xs: Seq[A], by: AnyRef) {
    def apply(f: A => Unit) = new Apply(f)

    class Apply(f: A => Unit) {
        def AndThen(k: Exit => Unit) {
            xs.forloop(x => by.synchronized{f(x)}, q => by.synchronized{k(q)})
        }
    }
}
