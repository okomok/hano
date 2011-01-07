

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Self() extends Context {
    override def exit(q: Exit) = ()
    override def forloop(f: Reaction[Unit]) {
        f.tryRethrow {
            f()
        }
        f.exitNothrow(Exit.End)
    }
}
