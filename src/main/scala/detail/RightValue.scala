

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


@deprecated("unneeded")
private[hano]
object RightValue {

    def get[A](lr: Either[Throwable, A]): A = lr match {
        case Left(t) => throw t
        case Right(r) => r
    }

    def maybe[A](r: A)(q: Exit): Either[Throwable, A] = q match {
        case Exit.Failed(t) => Left(t)
        case q => Right(r)
    }

}
