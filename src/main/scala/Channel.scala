

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
        readLock.lock()
        val v = try {
            if (readNode.next == null) {
                writeLock.lock()
                try {
                    if (readNode.next == null) {
                        readNode.next = new Node[A]
                    }
                } finally {
                    writeLock.unlock()
                }
            }

            val w = readNode.value
            readNode = readNode.next
            w
        } finally {
            readLock.unlock()
        }
        v.forloop(f)
    }

    def read = toCps

    def write(x: A) {
        writeLock.lock()
        val v = try {
            val w = writeNode.value
            if (writeNode.next == null) {
                writeNode.next = new Node[A]
            }
            writeNode = writeNode.next
            w
        } finally {
            writeLock.unlock()
        }
        v := x
    }

    def loop: Seq[A] = {
        new Channel.LoopOther(this)
        /*
        if (context eq Context.self) {
            new Channel.LoopSelf(this)
        } else {
            new Channel.LoopOther(this)
        }
        */
    }
}


private[hano]
object Channel {

    private class LoopOther[A](_1: Channel[A]) extends Resource[A] {
        assert(_1.context ne Context.self)
        @volatile private[this] var isActive = true
        override def context = _1.context
        override protected def closeResource() = isActive = false
        override protected def openResource(f: Reaction[A]) {
            val _k = detail.ExitOnce { q => f.exit(q) }

            def rec() {
                _1 `for` { x =>
                    _k.beforeExit {
                        f(x)
                        if (isActive) {
                            rec()
                        } else {
                            _k(Exit.Closed)
                        }
                    }
                } exit {
                    case q @ Exit.Failed(_) => _k(q)
                    case _ => ()
                }
            }
            rec()
        }
    }

    /*
    // Avoid stack-overflow in case write is bazillion before read.
    private class LoopSelf[A](_1: Channel[A]) extends Seq[A] {
        assert(_1.context eq Context.self)
        override def context = _1.context
        override def forloop(f: Reaction[A]) {
            val _k = detail.ExitOnce { q => f.exit(q) }

            @annotation.tailrec
            def g() {
                var y: Option[A] = None
                _1 `for` { x =>
                    _k.beforeExit {
                        y = Some(x)
                    }
                } exit {
                    case q @ Exit.Failed(_) => f.exit(q)
                    case _ => ()
                }
                if (!y.isEmpty) {
                    f(y.get)
                    g()
                }
            }
            g()
        }
    }
    */
}
