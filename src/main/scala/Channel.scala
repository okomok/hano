

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * A mutable one-element sequence; As you foreach, element varies.
 */
final class Channel[A] extends Seq[A] with java.io.Closeable {
    private[this] val _fs = new java.util.ArrayDeque[Reaction[A]]
    private[this] val _xs = new java.util.ArrayDeque[A]
    private[this] val _live = new Util.Live(_a ! Close, new Channel.ClosedException)

    private case class OnWriteMsg(f: Reaction[A])
    private case class WriteMsg(x: A)

    private[this] lazy val _a = {
        val a = new scala.actors.Reactor[Any] {
            override def act = {
                loop {
                    react {
                        case Action(f) => f()
                        case Close => {
                            detail.Polleach(_fs) { f =>
                                f.fail(new Channel.ClosedException)
                            }
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
                                    Exit.Empty
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
        a.start()
    }

    override def close() = _live.die()

    override lazy val process = _live {
        new Async(_a).asProcess
    }

    /**
     * Will call a reaction when a value is written.
     */
    override def forloop(f: Reaction[A]) = _live {
        _a ! OnWriteMsg(f)
    }

    /**
     * Writes a value.
     */
    def write(x: A) = _live {
        _a ! WriteMsg(x)
    }

    /**
     * Reads and removes a value.
     */
    def read(implicit _timeout: Util.Default[Long] = NO_TIMEOUT): A = _live {
        val v = new Val[A]
        forloop(v)
        v.get(_timeout)
    }

    /**
     * Writes all the sequence values.
     */
    def output(xs: Seq[A]): this.type = _live[this.type] {
        xs.forloop(toReaction)
        this
    }

    @annotation.compatibleConversion
    def toReaction: Reaction[A] = new Channel.ToReaction(this)

    @annotation.aliasOf("output")
    def <<(xs: Seq[A]): this.type = output(xs)
}


object Channel {

    /**
     * Sent when a `Channel` closed.
     */
    class ClosedException extends IllegalStateException


    private class ToReaction[A](_1: Channel[A]) extends Reaction[A] {
        override def rawApply(x: A) = _1.write(x)
    }
}
