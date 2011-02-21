

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Times[A](_1: Seq[A], _2: IntVar) extends SeqProxy[A] {
    override val self = {
        if (_1.context eq Self) {
            new TimesSelf(_1, _2)
        } else {
            new TimesOther(_1, _2)
        }
    }
}


private[hano]
class TimesOther[A](_1: Seq[A], _2: IntVar, grainSize: Int = 1) extends SeqResource[A] {
    assert(_1.context ne Self)

    private[this] var isActive = false
    override def context = _1.context
    override def closeResource() { isActive = false; _1.close() }
    override def openResource(f: Reaction[A]) {
        assert(!isActive)

        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        val _c = _2.Copy

        def rec() {
            if (!_c.postDec) {
                context eval {
                    _k(Exit.End)
                }
                return
            }

            _1 onEach { x =>
                synchronized { // is needed because...
                    f beforeExit {
                        var i = 0
                        while (isActive && (i != grainSize)) {
                            i += 1
                            f(x) // may reenter `forloop` from other threads.
                        }
                        if (!isActive) {
                            _k(Exit.Closed)
                            // `isActive` is out of play for me.
                        }
                    }
                }
                // Now you can reenter `forloop`.
            } onExit { q =>
                f beforeExit { // Don't refer `isActive` after `_k`
                    q match {
                        case Exit.End => {
                            if (isActive) {
                                rec()
                            }
                        }
                        case q => _k(q)
                    }
                }
            } start()
        }

        rec()
    }
}


// Specialized to avoid stack-overflow.
private[hano]
class TimesSelf[A](_1: Seq[A], _2: IntVar) extends Seq[A] {
    assert(_1.context eq Self)

    @volatile private[this] var isActive = false
    override def context = _1.context
    override def close() { isActive = false; _1.close() }

    // requires synchronized in case close-then-forloop from other threads.
    override def forloop(f: Reaction[A]) = synchronized {
        assert(!isActive)

        isActive = true
        def _k(q: Exit) { close(); f.exit(q) }

        val _c = _2.Copy

        @scala.annotation.tailrec
        def rec() {
            if (!_c.postDec) {
                context eval {
                    _k(Exit.End)
                }
                return
            }

            _1.noEnd onEach {
                f(_)
            } onExit {
                _k(_)
            } start()

            if (isActive) {
                rec()
            } else {
                _k(Exit.Closed)
            }
        }

        rec()
    }
}
