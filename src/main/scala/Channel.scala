

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent.locks.ReentrantLock
import detail.Synchronized


/**
 * Asynchronous channel
 * This is mutable one-element sequence; As you foreach, element varies.
 */
final class Channel[A](override val context: Context = async) extends Seq[A] {
    require(context ne Self)
    require(context ne Unknown)

    private class Node[A] {
        val value = new Val[A](context) // shares context.
        var next: Node[A] = null
    }

    private[this] var readNode = new Node[A]
    private[this] var writeNode = readNode

    private[this] val readLock = new ReentrantLock
    private[this] val writeLock = new ReentrantLock

    /**
     * Will call a reaction when a value is written.
     */
    override def forloop(f: Reaction[A]) = _readable.forloop(f)

    /**
     * Writes a value.
     */
    def write(x: A) = _writable.set(x)

    /**
     * Reads and removes a value.
     */
    def read: A = _readable.apply()

    /**
     * Writes a value as single-element sequence.
     */
    def output(x: Seq[A]): this.type = { _writable.assign(x); this }

    @annotation.aliasOf("output")
    def <<(x: Seq[A]): this.type = output(x)

    private def _readable: Val[A] = Synchronized(readLock) {
        if (readNode.next == null) {
            Synchronized(writeLock) {
                if (readNode.next == null) {
                    readNode.next = new Node[A]
                }
            }
        }
        val w = readNode.value
        readNode = readNode.next
        w
    }

    private def _writable: Val[A] = Synchronized(writeLock) {
        val w = writeNode.value
        if (writeNode.next == null) {
            writeNode.next = new Node[A]
        }
        writeNode = writeNode.next
        w
    }
}
