

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class WrappedReaction[A](override val self: Reaction[A]) extends ReactionProxy[A]
