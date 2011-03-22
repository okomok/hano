

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}
import scala.collection.mutable.Builder


private[hano]
trait Defaults { self: Seq.type =>
    @annotation.equivalentTo("new LinkedBlockingQueue[Any](20)")
    def defaultBlockingQueue: BlockingQueue[Any] = new LinkedBlockingQueue[Any](20)

    @annotation.equivalentTo("Vector.newBuilder[A]")
    def defaultCopyBuilder[A]: Builder[A, Vector[A]] = Vector.newBuilder[A]
}
