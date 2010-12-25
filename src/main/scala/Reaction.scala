

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait Reaction[-A] {

    def react(a: A): Unit

    def onExit(e: Exit): Unit

}
