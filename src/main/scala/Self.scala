

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Call-site context
 */
object Self extends Context {
    override def close() = ()

    override def forloop(f: Reaction[Unit]) {
        f.enter {
            Exit { q  =>
                f.exit(Exit.Failure(Exit.ByOther(q)))
            }
        }

        f.applying {
            f()
        }

        f.exit(Exit.Success)
    }
}
