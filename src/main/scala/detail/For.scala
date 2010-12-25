

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
case class For[A](xs: Seq[A]) {
    def apply(f: A => Unit) = new Apply(f)

    class Apply(f: A => Unit) {
        def AndThen(k: Exit => Unit){
            xs.forloop(Reaction(f, k))
        }
    }
}

private[hano]
case class LockedFor[A](xs: Seq[A], by: AnyRef) {
    def apply(f: A => Unit) = new Apply(f)

    class Apply(f: A => Unit) {
        def AndThen(k: Exit => Unit) {
            xs.forloop(Reaction(x => by.synchronized{f(x)}, q => by.synchronized{k(q)}))
        }
    }
}
