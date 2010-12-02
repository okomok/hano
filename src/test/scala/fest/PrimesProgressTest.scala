

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package festtest
package sampleprogresstest


import org.testng.annotations._
import org.fest.swing.fixture.FrameFixture

import javax.swing
import java.awt
import com.github.okomok.hano
import FestUtil.textAsName
import hano.Swing
import hano.eval


class PrimesProgressGuiTest
//    extends NotFestSuite
    extends FestTestNGSuite
{
    private var fx: FrameFixture = null
    private val gate = new java.util.concurrent.CountDownLatch(1)

    // See: http://haskell.org/haskellwiki/Prime_numbers
    val primes = 2 #:: 3 #:: Stream.iterate(5)(n => n + 2).filter(isPrime(_))
    def isPrime(n: Int): Boolean = primes.tail.takeWhile(p => p*p <= n).forall(notDivs(n, _))
    def notDivs(n: Int, p: Int): Boolean = n % p != 0

    val Q = 10000
    val A = 104729 // 10000th prime

    override protected def onSetUp {
        val f = eval.InEdt {
            val frame = new swing.JFrame("ProgressMonitor Sample")
            frame.setLayout(new awt.GridLayout(0, 1))
            val startButton = new swing.JButton("Start") textAsName
            val resultLabel = new swing.JLabel("Result") textAsName;
            frame.add(startButton)
            frame.add(resultLabel)

            for (actionEvent <- Swing.ActionPerformed(startButton)) {
                val parent = actionEvent.getSource.asInstanceOf[awt.Component]
                val monitor = new swing.ProgressMonitor(parent, "Loading Progress", "Getting Started...", 0, Q)
                monitor.setMillisToDecideToPopup(0)
                monitor.setMillisToPopup(0)

                val ps = hano.Seq.origin {
                    eval.Async // primes in thread-group.
                } catching {
                    case t: Throwable => t.printStackTrace()
                } generate {
                    primes
                } onClose {
                    gate.countDown()
                } shift {
                    eval.InEdt // reactions in EDT.
                }

                ps.onNth(Q-1) { p =>
                    resultLabel.setText(p.toString)
                    ps.close()
                }.indices.stepTime(100).foreach { i =>
                    if (monitor.isCanceled) {
                        resultLabel.setText("Canceled")
                        ps.close()
                    } else {
                        monitor.setProgress(i)
                        monitor.setNote("Calculated " + i + " primes")
                    }
                }
            }

            frame.setSize(300, 200)
            frame.setVisible(true)
            frame
        }

        fx = new FrameFixture(robot, f())
        fx.show()
    }

    @Test def testTrivial {
        fx.button("Start").click()
        gate.await()
        fx.label("Result").requireText(A.toString)
    }
}