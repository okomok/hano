

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
import hano.{Swing, Beans}
import FestUtil.textAsName


class SampleProgressGuiTest
    extends NotFestSuite
//    extends FestTestNGSuite
{
    private var fx: FrameFixture = null
    private var progress = 0
    private var monitor: swing.ProgressMonitor = null
    private var timer = new swing.Timer(250, null)

    override protected def onSetUp {
        val f = hano.InEdt.future {
            val frame = new swing.JFrame("ProgressMonitor Sample")
            frame.setLayout(new awt.GridLayout(0, 1))
            val startButton = new swing.JButton("Start")
            for (actionEvent <- Swing.ActionPerformed(startButton)) {
                val parent = actionEvent.getSource.asInstanceOf[awt.Component]
                monitor = new swing.ProgressMonitor(parent, "Loading Progress", "Getting Startet...", 0, 200)
                progress = 0
            }
            frame.add(startButton)

            val increaseButton = new swing.JButton("Manual Increase")
            for (actionEvent <- Swing.ActionPerformed(increaseButton)) {
                if (monitor != null) {
                    if (monitor.isCanceled) {
                        println("Monitor canceled")
                    } else {
                        progress += 5
                        monitor.setProgress(progress)
                        monitor.setNote("Loaded " + progress + " files")
                    }
                }
            }
            frame.add(increaseButton)

            val autoIncreaseButton = new swing.JButton("Automatic Increase")
            for (actionEvent <- Swing.ActionPerformed(autoIncreaseButton)) {
                if (monitor != null) {
                    for (actionEvent <- Swing.ActionPerformed(timer)) {
                        println("timer event")
                        if (monitor != null) {
                            if (monitor.isCanceled) {
                                println("Monitor canceled")
                                timer.stop
                            } else {
                                progress += 3
                                monitor.setProgress(progress)
                                monitor.setNote("Loaded " + progress + " files")
                            }
                        }
                    }
                    timer.start
                }
            }
            frame.add(autoIncreaseButton)

            frame.setSize(300, 200)
            frame.setVisible(true)
            frame
        }

        fx = new FrameFixture(robot, f())
        fx.show()
    }

    @Test def testTrivial {
        Thread.sleep(20000)
        /*
        for (_ <- 0 until 5) {
            fx.button("Select Me").click
            val bk1 = fx.button("Select Me").background
            bk1.requireEqualTo(bk1.target)
            fx.button("No Select Me").background.requireEqualTo(bk1.target)
        }
        */
    }

    @AfterTest def stopTimer {
        timer.stop
    }
}