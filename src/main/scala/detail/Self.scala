

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Self() extends Context {
    override def exit(q: Exit) = ()
    override def forloop(f: Reaction[Unit]) {
        try {
            f()
        } catch {
            case t: Throwable => {
                f.exit(Exit.Failed(t)) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
        f.exit(Exit.End)
    }
}
