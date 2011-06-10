

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


object ManagedAwait {
    def apply(c: java.util.concurrent.CountDownLatch, xs: Seq[_]) {
        xs.process match {
            case p: Async => {
                p.out match {
                    case r: Async.Out => {
                        r.scheduler.managedBlock {
                            new scala.concurrent.ManagedBlocker {
                                override def block(): Boolean = { c.await(); true }
                                override def isReleasable: Boolean = { c.getCount == 0 }
                            }
                        }
                    }
                    case _ => c.await()
                }
            }
            case _ => c.await()
        }
    }
}
