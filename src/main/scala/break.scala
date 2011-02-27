

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object break {
    /**
     * Breaks the current reaction.
     */
    def apply(): Nothing = throw Control

    /**
     * Thrown by `break()`.
     */
    object Control extends scala.util.control.ControlThrowable
}
