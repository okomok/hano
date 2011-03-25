

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * A mutable one-element sequence; As you foreach, element varies.
 */
final class Channel[A] extends Seq[A] with java.io.Closeable {
    private[this] val _fs = new java.util.concurrent.ConcurrentLinkedQueue[Reaction[A]]
    private[this] val _xs = new java.util.ArrayDeque[A]

    private case class OnWriteMsg(f: Reaction[A])
    private case class WriteMsg(x: A)

    private[this] val _a = new scala.actors.Reactor[Any] {
        override def act = {
            loop {
                react {
                    case Action(f) => f()
                    case Close => {
                        for (f <- Iter.from(_fs).able) {
                            f.exit(Exit.Failure(new Channel.ClosedException))
                        }
                        _fs.clear()
                        _xs.clear()
                        exit()
                    }

                    case OnWriteMsg(f) => {
                        val x = _xs.poll
                        if (x != null) {
                            f.enter {
                                Exit.Empty
                            } applying {
                                f(x)
                            } exit {
                                Exit.Success
                            }
                        } else {
                            _fs.offer(f)
                            f.enter {
                                Exit { _ =>
                                    _fs.remove(f) // might be a bottleneck.
                                }
                            }
                        }
                    }
                    case WriteMsg(x) => {
                        val f = _fs.poll
                        if (f != null) {
                            f.applying {
                                f(x)
                            } exit {
                                Exit.Success
                            }
                        } else {
                            _xs.offer(x)
                        }
                    }
                }
            }
        }
    }
    _a.start()

    override def close(): Unit = _a ! Close

    override val process = new detail.Async(_a).asProcess

    /**
     * Will call a reaction when a value is written.
     */
    override def forloop(f: Reaction[A]) = _a ! OnWriteMsg(f)

    /**
     * Writes a value.
     */
    def write(x: A) = _a ! WriteMsg(x)

    /**
     * Reads and removes a value.
     */
    def read(_timeout: Long = INF): A = {
        val v = new Val[A]
        forloop(v)
        v.get(_timeout)
    }

    /**
     * Writes all the sequence values without `Exit.Success`.
     */
    def output(that: Seq[A]): this.type = {
        that.onEach {
            write(_)
        } start()
        this
    }

    @annotation.aliasOf("output")
    def <<(xs: Seq[A]): this.type = output(xs)
}


object Channel {

    /**
     * Sent when a `Channel` closed.
     */
    class ClosedException extends IllegalStateException
}
