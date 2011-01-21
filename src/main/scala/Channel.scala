

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent.locks.ReentrantLock


/**
 * Asynchronous channel
 * This is mutable one-element sequence; As you foreach, element varies.
 */
final class Channel[A](override val context: Context = Context.act) extends Seq[A] {
    require(context ne Context.self)

    private class Node[A] {
        val value = new Val[A](context) // shares context.
        var next: Node[A] = null
    }

    private[this] var readNode = new Node[A]
    private[this] var writeNode = readNode

    private[this] val readLock = new ReentrantLock
    private[this] val writeLock = new ReentrantLock

    override def forloop(f: Reaction[A]) {
        detail.Synchronized(readLock) {
            if (readNode.next == null) {
                detail.Synchronized(writeLock) {
                    if (readNode.next == null) {
                        readNode.next = new Node[A]
                    }
                }
            }
            val w = readNode.value
            readNode = readNode.next
            w
        } forloop(f)
    }

    def write(x: A) {
        detail.Synchronized(writeLock) {
            val w = writeNode.value
            if (writeNode.next == null) {
                writeNode.next = new Node[A]
            }
            writeNode = writeNode.next
            w
        } := x
    }

    @Annotation.aliasOf("toCps")
    def read = toCps

    @Annotation.aliasOf("write")
    def !(x: A) = write(x)

    @Annotation.equivalentTo("f(Sync.head(this)())")
    def receive[R](f: A => R): R = f(Sync.head(this)())
}
