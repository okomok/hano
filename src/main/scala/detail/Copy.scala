

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Copy[A, To](_1: Seq[A], _2: scala.collection.generic.CanBuildFrom[Nothing, A, To])
    extends SeqAdapter[To]
{
    override protected val underlying = _1

    override def forloop(f: Reaction[To]) {
        var b = _2()
        _1.onEnter {
            f.enter(_)
        } onEach {
            b += _
        } onExit {
            case Exit.End => {
                f(b.result)
                f.exit(Exit.End)
            }
            case q => f.exit(q)
        } start()
    }
}
