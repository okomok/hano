

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent.locks.ReentrantLock


/**
 * Asynchronous channel
 */
final class Channel[A](override val context: Context = Context.self) extends Seq[A] {

    private class Node[A] {
        val value = new Val[A](context) // shares context.
        var next: Node[A] = null
    }

    private[this] var headNode = new Node[A]
    private[this] var lastNode = headNode

    private[this] val headLock = new ReentrantLock
    private[this] val lastLock = new ReentrantLock

    override def forloop(f: Reaction[A]) {
        headLock.lock()
        val v = try {
            if (headNode.next == null) {
                lastLock.lock()
                try {
                    if (headNode.next == null) {
                        headNode.next = new Node[A]
                    }
                } finally {
                    lastLock.unlock()
                }
            }

            val w = headNode.value
            headNode = headNode.next
            w
        } finally {
            headLock.unlock()
        }
        v.forloop(f)
    }

    def read = toCps

    def write(x: A) {
        lastLock.lock()
        val v = try {
            val w = lastNode.value
            if (lastNode.next == null) {
                lastNode.next = new Node[A]
            }
            lastNode = lastNode.next
            w
        } finally {
            lastLock.unlock()
        }
        v := x
    }
}
