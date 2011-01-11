

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class For[+A](xs: Seq[A], f: A => Unit) {
    def exit(k: Exit => Unit = For.defaultExitHandler){
        xs.forloop(Reaction(f, k))
    }
}

private[hano]
object For {
    object defaultExitHandler extends (Exit => Unit) {
        override def apply(q: Exit) = q match {
            case Exit.Failed(t) => LogErr(t, "unhandled error in `for`")
            case _ => ()
        }
    }
}
