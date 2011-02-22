

// Copyright Shunsuke Sogame 2010.
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
    override def forloop(f: Reaction[A]): Unit = _readable.forloop(f)

    /**
     * Writes the value.
     */
    def write(x: A): Unit = _writable.set(x)

    /**
     * Reads and removes the value.
     */
    def read: A = _readable.apply()

    /**
     * Sends a value as single-element sequence.
     */
    def send(that: Seq[A]): Unit = _writable.assign(that)

    @annotation.aliasOf("send")
    def !(that: Seq[A]): Unit = send(that)

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
