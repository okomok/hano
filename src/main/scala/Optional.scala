

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Optional {

    @Annotation.equivalentTo("try { Some(body) } catch { case _ => None }")
    def apply[A](body: => A): Seq[A] = Seq.from {
        try {
            Some(body)
        } catch {
            case _ => None
        }
    }
}
