

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FillTime(_1: Seq[_], _2: Long) extends SeqAdapter[Unit] {
    override protected val underlying = _1
    override def forloop(f: Reaction[Unit]) {
        var u: Seq[Unit] = null
        def fill() {
            u = FillTime.repeatIn(_2)
            u onEach { _ =>
                f()
            } start()
        }
        fill()

        _1 onEach { _ =>
            u.close()
            fill()
        } onExit { q =>
            u.close()
            FillTime.timer eval {
                f.exit(q)
            }
        } start()
    }

    override def fillTime(i: Long): Seq[Unit] = _1.fillTime(java.lang.Math.min(_2, i)) // fillTime.fillTime fusion
}

private[hano]
object FillTime {
    private val timer = new Timer(true)
    private def repeatIn(i: Long): Seq[Unit] = timer.schedule(i, i)
}
